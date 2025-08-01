package com.by.dallinday.run.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RunPostRequest {

    private String courseId;

    private Double distance;

    private Double duration;

    private Double pace;

    private Double calorie;

    private Double accuracy;

    private String startTime;

    private String endTime;
}
