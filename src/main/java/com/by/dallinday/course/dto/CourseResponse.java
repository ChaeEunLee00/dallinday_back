package com.by.dallinday.course.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseResponse {

    private Long courseId;

    private String name;          // "둘레길 코스"

    private String description;   // 요약 설명

    private Double distance;      // km

    private Integer duration;     // 분

    private Integer difficulty;   // 1~3

    private String gpxpath;

    // spots 배열: spotId, name, mapx, mapy
    private List<CourseSpotResponse> spots = new ArrayList<>();

    // 러닝 기록 목록
    private List<CourseRunResponse> runList = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
