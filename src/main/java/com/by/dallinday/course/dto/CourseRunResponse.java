package com.by.dallinday.course.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRunResponse {
    private Long runId;

    private Long memberId;

    private Double distance;

    private Double duration;
}
