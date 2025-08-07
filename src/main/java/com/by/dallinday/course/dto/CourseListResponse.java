package com.by.dallinday.course.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CourseListResponse {

    private String courseId;

    private String crsKorNm; // 코스명

    private double crsDstnc; // 거리 (단위: km)

    private int crsTotlRqrmHour; // 소요시간 (단위: 분)

    private int crsLevel; // 난이도 (1~3 등급)

    private String crsTourInfo; // 루트 정보

}
