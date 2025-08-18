package com.by.dallinday.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseSpotResponse {
    private Long spotId;

    private String name;

    private Double longitude;  // 경도

    private Double latitude;  // 위도
}
