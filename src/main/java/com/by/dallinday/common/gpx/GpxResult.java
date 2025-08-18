package com.by.dallinday.common.gpx;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GpxResult {
    List<double[]> path;   // [위도, 경도
    // ]
    List<Double> segLen;   // path[i-1]→path[i] 구간 길이(m)

    List<Double> prefix;   // 시작점부터 i번째 점까지 누적거리(m), prefix[0]=0

    Double distanceKm;     // 총거리(km)

    Integer movingMinutes;    // 이동시간(분) : 정지 구간 제외

    Double totalAscent;    // 총상승고도(m)

    Double maxGrade;       // 최대 경사(%)

    Integer difficulty;         // 1~3
}
