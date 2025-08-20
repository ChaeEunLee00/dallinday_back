package com.by.dallinday.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseTop5Response {
        private Long courseId;

        private String name; // 코스명

        private Double distance; // 거리 (단위: km)

        private Integer duration; // 소요시간 (단위: 분)

        private Integer runCount; // 달리기 기록 갯수
}
