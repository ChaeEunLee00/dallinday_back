package com.by.dallinday.course;

import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.common.gpx.DistanceUtil;
import com.by.dallinday.common.gpx.GpxParser;
import com.by.dallinday.spot.SpotAPIClient;
import com.by.dallinday.spot.SpotItem;
import io.jenetics.jpx.WayPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseMapper courseMapper;
    private final CourseAPIClient courseAPIClient;
    private final CourseRepository courseRepository;

    private final SpotAPIClient spotAPIClient;

    // 코스 조회
    public Course findCourse() {
        return new Course();
    }

    // 관광지 별 코스 리스트 조회
    public List<CourseListResponse> findSpotCourseList(Long spotId) {
        // spot 좌표 가져오기
        SpotItem spotItem = spotAPIClient.callContentIdBasedAPI(spotId);
        double spotLat = spotItem.getMapy();
        double spotLon = spotItem.getMapx();

        double radius = 10000.0; // 10km 기준

        List<Course> allCourses = courseRepository.findAll();
        List<Course> matchedCourses = new ArrayList<>();

        for (Course course : allCourses) {
            try {
                List<WayPoint> points = GpxParser.extractCoordinates(course.getGpxpath());

                boolean withinRadius = points.stream().anyMatch(point ->
                        DistanceUtil.haversine(
                                point.getLatitude().doubleValue(),
                                point.getLongitude().doubleValue(),
                                spotLat,
                                spotLon
                        ) < radius
                );

                if (withinRadius) {
                    matchedCourses.add(course);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Course -> CourseListResponse 맵핑
        return matchedCourses.stream()
                .map(course -> courseMapper.courseCourseListResponse(course))
                .toList();
    }

    // 테마 별 코스 리스트 조회
    public Course findThemeCourseList() {
        return new Course();
    }

    // TourAPI를 통해 가져온 코스 데이터 저장 및 동기화
    public void syncCourses() {
        int page = 1, size = 10; // 가져올 데이터 수
        List<CourseItem> courseItems = courseAPIClient.callCourseAPI(size, page);

        for (CourseItem item : courseItems) {
            // 이미 존재하는 코스인지 확인
            Optional<Course> foundCourse = courseRepository.findById(item.getCourseId());
            if (foundCourse.isPresent()) {
                // 이미 있다면 업데이트
                Course course = foundCourse.get();
                course.updateFrom(item);
            } else {
                // 없으면 새로 저장
                Course newCourse = Course.from(item);
                courseRepository.save(newCourse);
            }
        }
    }
}
