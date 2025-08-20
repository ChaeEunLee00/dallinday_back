package com.by.dallinday.run.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RunPostRequest {
    private Long memberId;

    private Long courseId;

    private Double distance;

    private Double duration;

    private Double pace;

    private Double calorie;

    private Double accuracy;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
