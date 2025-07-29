package com.by.dallinday.course;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseItem {

    private String routeIdx;

    private String crsIdx;

    private String crsKorNm; // 코스명

    private double crsDstnc; // 거리 (단위: km)

    private int crsTotlRqrmHour; // 소요시간 (단위: 분)

    private int crsLevel; // 난이도 (1~3 등급)

    private String crsCycle; // 순환형 여부 (예: 순환형, 비순환형)

    private String crsContents; // 상세 설명

    private String crsSummary; // 요약 설명

    private String crsTourInfo; // 관광 정보

    @JsonProperty("travelerinfo")
    private String travelerInfo; // 여행자 정보

    private String sigun; // 지역

    private String brdDiv; // 분류 코드

    private String gpxpath; // GPX 파일 경로

    private String createdtime; // 생성일시 (yyyyMMddHHmmss)

    private String modifiedtime; // 수정일시 (yyyyMMddHHmmss)
}
