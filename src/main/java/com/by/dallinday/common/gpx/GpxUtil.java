package com.by.dallinday.common.gpx;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.course.Course;
import com.by.dallinday.courseSpot.CourseSpot;
import com.by.dallinday.spot.tourAPI.SpotItem;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Length;
import io.jenetics.jpx.WayPoint;
import org.springframework.stereotype.Component;

@Component
public class GpxUtil {

    private static final double RADIUS_M = 300.0;
    private static final int ELEV_SMOOTH_WINDOW = 5; // 이동평균 윈도우
    private static final double MIN_ELEV_DELTA = 3.0; // 총상승에 반영할 최소 고도 변화(m)
    private static final double MIN_SEG_DIST_M = 5.0; // 경사 계산 시 고려 최소 수평 거리(m)
    private static final double RUN_BASE_KMH = 10.0; // 러닝 기준 기본 평균 속도(km/h) — 가장 단순한 추정값 6:00 페이스

    //GPX 파일(URL) 분석: 거리/시간/난이도 + 경로 파생값(path/segLen/prefix) 반환
    public GpxResult analyzeGpx(String gpxUrl) {
        // 1) 포인트 추출
        List<WayPoint> pts;
        try{
            pts = extractCoordinates(gpxUrl);
        } catch (Exception io) {
            throw new BusinessLogicException(ExceptionCode.GPX_ANALYZE_ERROR);
        }

        // 2) 위경도 있는 포인트만
        List<WayPoint> filtered = pts.stream()
                .filter(p -> p.getLatitude() != null && p.getLongitude() != null)
                .toList();
        if (filtered.size() < 2) throw new BusinessLogicException(ExceptionCode.GPX_ANALYZE_ERROR);

        // 3) 경로 좌표 [lat,lon]
        List<double[]> path = filtered.stream()
                .map(p -> new double[]{p.getLatitude().doubleValue(), p.getLongitude().doubleValue()})
                .toList();

        // 4) 구간 길이/총거리
        List<Double> segLen = new ArrayList<>();
        double totalDistM = 0;
        for (int i = 1; i < path.size(); i++) {
            double d = DistanceUtil.haversine(
                    path.get(i - 1)[0], path.get(i - 1)[1],
                    path.get(i)[0], path.get(i)[1]
            );
            segLen.add(d);
            totalDistM += d;
        }

        // 5) 누적거리 prefix
        List<Double> prefix = new ArrayList<>();
        double acc = 0;
        prefix.add(0.0);
        for (double d : segLen) {
            acc += d;
            prefix.add(acc);
        }

        // 6) 이동시간
        double baseMps = RUN_BASE_KMH / 3.6;            // km/h -> m/s
        long movingS = Math.max(1L, Math.round(totalDistM / baseMps));

        // 7) 고도 처리(결측 메움 → 이동평균)
        List<Double> ele = filtered.stream().map(p -> p.getElevation().map(Length::doubleValue).orElse(Double.NaN)).toList();
        List<Double> eleFilled = fillElevation(ele);
        List<Double> eleSmooth = movingAverage(eleFilled, ELEV_SMOOTH_WINDOW);

        // 8) 총상승/최대경사
        double ascent = 0;
        double maxGrade = 0;
        for (int i = 1; i < eleSmooth.size(); i++) {
            double de = eleSmooth.get(i) - eleSmooth.get(i - 1);
            if (de > MIN_ELEV_DELTA) ascent += de;

            double d = segLen.get(i - 1);
            if (d >= MIN_SEG_DIST_M) {
                double g = Math.max(0, de) / d * 100.0;
                if (g > maxGrade) maxGrade = g;
            }
        }

        // 9) 난이도(0~100 점수 → 1~3 등급)
        double score = 0.45 * normalize(ascent, 0, 2000)
                + 0.35 * normalize(totalDistM / 1000.0, 0, 50)
                + 0.20 * normalize(maxGrade, 0, 25);
        score = Math.max(0, Math.min(1, score));
        int difficulty = (score < 0.30) ? 1 : (score < 0.60 ? 2 : 3);

        return new GpxResult(path, segLen, prefix, totalDistM / 1000.0,
                (int) movingS / 60, ascent, maxGrade, difficulty);
    }

    /**
     * 스팟 목록을 경로에 “사영”해서 반경 이내만 고른 뒤, 경로 순으로 정렬해 CourseSpot 리스트 생성
     * - 동점 방지: 정렬은 실수형 alongMeters로 하고, 저장용 orderIndex는 0..N-1로 재부여
     */
    public List<CourseSpot> pickSpots(Course course, GpxResult result, List<SpotItem> spots) {
        // 후보 수집: (spotId, alongMeters, minDistMeters, name, lon, lat)
        class Sel {
            Long spotId; double along; double dist;
            String name; Double lon; Double lat;
            Sel(Long s, double a, double d, String n, Double x, Double y) {
                spotId = s; along = a; dist = d; name = n; lon = x; lat = y;
            }
        }
        List<Sel> candidates = new ArrayList<>();

        for (SpotItem s : spots) {
            Nearest near = nearestOnPath(s.getMapy(), s.getMapx(), result.getPath(), result.getSegLen(), result.getPrefix());
            if (near.minDistanceMeters <= RADIUS_M) {
                candidates.add(new Sel( s.getSpotId(), near.alongMeters, near.minDistanceMeters,
                        s.getTitle(), s.getMapx(), s.getMapy()));
            }
        }

        // 정렬: 경로 진행 순 → 더 가까운 스팟
        candidates.sort(Comparator
                .comparingDouble((Sel x) -> x.along)
                .thenComparingDouble(x -> x.dist));

        // 결과 생성: orderIndex = 0..N-1(경로 순서)
        List<CourseSpot> courseSpots = new ArrayList<>();
        int idx = 0;
        for (Sel s : candidates) {
            CourseSpot cs = new CourseSpot();
            cs.setCourse(course);
            cs.setSpotId(s.spotId);
            cs.setOrderIndex(idx++);
            cs.setName(s.name);
            cs.setLongitude(s.lon);
            cs.setLatitude(s.lat);
            courseSpots.add(cs);
        }
        return courseSpots;
    }

    // ===== 내부 함수 =====

    // 결측/NaN 고도값을 이웃값으로 메움(앞/뒤 보간에 준함)
    private List<Double> fillElevation(List<Double> ele) {
        List<Double> out = new ArrayList<>(ele.size());
        Double last = null;
        for (Double v : ele) {
            if (v == null || v.isNaN()) out.add(last);
            else { out.add(v); last = v; }
        }
        double first = 0;
        if (!out.isEmpty() && (out.get(0) == null || out.get(0).isNaN())) {
            for (Double v : out) { if (v != null && !v.isNaN()) { first = v; break; } }
            for (int i = 0; i < out.size(); i++) {
                if (out.get(i) == null || out.get(i).isNaN()) out.set(i, first);
            }
        }
        return out;
    }

    // 이동평균 스무딩(k 윈도우)
    private List<Double> movingAverage(List<Double> data, int k) {
        if (k <= 1) return data;
        int n = data.size();
        List<Double> out = new ArrayList<>(n);
        double sum = 0;
        int w = 0;
        for (int i = 0; i < n; i++) {
            sum += data.get(i);
            w++;
            if (i >= k) { sum -= data.get(i - k); w--; }
            out.add(sum / w);
        }
        return out;
    }

    private double normalize(double v, double min, double max) {
        if (v <= min) return 0;
        if (v >= max) return 1;
        return (v - min) / (max - min);
    }

    // 스팟(점)을 경로의 각 선분에 직교 투영하여 최근접 거리와 경로 누적거리(alongMeters)를 구함
    private record Nearest(double alongMeters, double minDistanceMeters) {}

    private Nearest nearestOnPath(double lat, double lon,
                                  List<double[]> path,
                                  List<Double> segLen,
                                  List<Double> prefix) {
        double minDist = Double.POSITIVE_INFINITY;
        int bestSeg = 0;
        double bestT = 0;

        for (int i = 1; i < path.size(); i++) {
            double[] a = path.get(i - 1);
            double[] b = path.get(i);
            Proj pd = pointToSegmentMeters(lat, lon, a[0], a[1], b[0], b[1]);
            if (pd.dist < minDist) {
                minDist = pd.dist;
                bestSeg = i - 1;
                bestT = pd.t;
            }
        }
        double along = prefix.get(bestSeg) + bestT * segLen.get(bestSeg);
        return new Nearest(along, minDist);
    }

    // 점-선분 최소거리(지역 평면 근사)
    private record Proj(double dist, double t) {}

    private Proj pointToSegmentMeters(double py, double px, double ay, double ax, double by, double bx) {
        double lat0 = Math.toRadians((ay + by + py) / 3.0);
        double rx = 6371000 * Math.toRadians(1) * Math.cos(lat0);
        double ry = 6371000 * Math.toRadians(1);

        double axm = ax * rx, aym = ay * ry;
        double bxm = bx * rx, bym = by * ry;
        double pxm = px * rx, pym = py * ry;

        double vx = bxm - axm, vy = bym - aym;
        double wx = pxm - axm, wy = pym - aym;

        double vv = vx * vx + vy * vy;
        double t = vv == 0 ? 0 : (wx * vx + wy * vy) / vv;
        if (t < 0) t = 0;
        else if (t > 1) t = 1;

        double cx = axm + t * vx, cy = aym + t * vy;
        double dx = pxm - cx, dy = pym - cy;
        double dist = Math.hypot(dx, dy);
        return new Proj(dist, t);
    }

    /**
     * URL의 GPX를 임시 파일로 내려받아 파싱 후 WayPoint 리스트로 반환
     */
    public List<WayPoint> extractCoordinates(String fileUrl) throws Exception {
        // 1. URL로부터 파일 다운로드
        URL url = new URL(fileUrl);
        Path tempFile = Files.createTempFile("temp-", ".gpx");

        InputStream in = url.openStream();
        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);

        // 2. 임시 파일에서 GPX 파싱
        GPX gpx = GPX.read(tempFile);
        Files.deleteIfExists(tempFile); // 사용 후 임시 파일 삭제

        return gpx.getTracks().stream()
                .flatMap(track -> track.getSegments().stream())
                .flatMap(segment -> segment.getPoints().stream())
                .toList();
    }
}
