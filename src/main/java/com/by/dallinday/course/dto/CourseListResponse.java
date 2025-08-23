package com.by.dallinday.course.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CourseListResponse {

    private Long courseId;

    private String name; // 코스명

    private String imageUrl; // 코스 이미지 - 시작지점 기준

    private Double distance; // 거리 (단위: km)

    private Integer duration; // 소요시간 (단위: 분)

    private Integer difficulty; // 난이도 (1~3 등급)

    private List<CourseSpotResponse> courseSpotList; // 루트 정보
}
