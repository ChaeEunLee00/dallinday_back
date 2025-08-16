package com.by.dallinday.course.dto;

import com.by.dallinday.run.dto.RunResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseResponse {

    private String courseId;

    private String crsKorNm;

    private Double crsDstnc;

    private Integer crsTotlRqrmMin;

    private Integer crsLevel;

    private String crsSummary;

    private String crsTourInfo;

    private String gpxpath;

    private String createdtime;

    private String modifiedtime;

    private List<CourseRunResponse> runList = new ArrayList<>();
}
