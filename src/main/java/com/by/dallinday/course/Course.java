package com.by.dallinday.course;

import com.by.dallinday.run.Run;
import com.by.dallinday.theme.Theme;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "courses")
public class Course {
    @Id
    private String courseId;

    @Column
    private String crsKorNm; // 코스명

    @Column
    private double crsDstnc; // 거리 (단위: km)

    @Column
    private int crsTotlRqrmHour; // 소요시간 (단위: 분)

    @Column
    private int crsLevel; // 난이도 (1~3 등급)

    @Lob
    @Column
    private String crsContents; // 상세 설명

    @Lob
    @Column
    private String crsSummary; // 요약 설명

    @Lob
    @Column
    private String crsTourInfo; // 관광 정보

    @Lob
    @Column
    private String travelerInfo; // 여행자 정보

    @Column
    private String sigun; // 지역

    @Column
    private String gpxpath; // GPX 파일 경로

    @Column
    private String createdtime; // 생성일시 (yyyyMMddHHmmss)

    @Column
    private String modifiedtime; // 수정일시 (yyyyMMddHHmmss)

//    @Column
//    private String routeId;

//    @Column
//    private String brdDiv; // 분류 코드

//    @Column
//    private String crsCycle; // 순환형 여부 (예: 순환형, 비순환형)

    @Column
    private String type;

    @Column
    private String spotId;

    @ManyToOne
    @JoinColumn(name = "themeId")
    private Theme theme;

    @OneToMany(mappedBy = "course")
    private List<Run> runList = new ArrayList<>();

    // courseItem으로부터 Course 객체 생성
    public static Course from(CourseItem item) {
        log.info("New course created: id={}, name={}", item.getCourseId(), item.getCrsKorNm());
        Course course = new Course();
        course.courseId = item.getCourseId();
        course.crsKorNm = item.getCrsKorNm();
        course.crsDstnc = item.getCrsDstnc();
        course.crsTotlRqrmHour = item.getCrsTotlRqrmHour();
        course.crsLevel = item.getCrsLevel();
        course.crsContents = item.getCrsContents();
        course.crsSummary = item.getCrsSummary();
        course.crsTourInfo = item.getCrsTourInfo();
        course.travelerInfo = item.getTravelerInfo();
        course.sigun = item.getSigun();
        course.gpxpath = item.getGpxpath();
        course.createdtime = item.getCreatedtime();
        course.modifiedtime = item.getModifiedtime();
        return course;
    }

    // CourseItem으로부터 기존 Course 객체 업데이트
    public void updateFrom(CourseItem item) {
        if (!this.modifiedtime.equals(item.getModifiedtime())) {
            log.info("Course updated: id={}, name={}", item.getCourseId(), item.getCrsKorNm());

            this.crsKorNm = item.getCrsKorNm();
            this.crsDstnc = item.getCrsDstnc();
            this.crsTotlRqrmHour = item.getCrsTotlRqrmHour();
            this.crsLevel = item.getCrsLevel();
            this.crsContents = item.getCrsContents();
            this.crsSummary = item.getCrsSummary();
            this.crsTourInfo = item.getCrsTourInfo();
            this.travelerInfo = item.getTravelerInfo();
            this.sigun = item.getSigun();
            this.gpxpath = item.getGpxpath();
            this.modifiedtime = item.getModifiedtime();
        }
    }
}
