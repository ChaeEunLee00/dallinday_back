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
    private Double crsDstnc; // 거리 (단위: km)

    @Column
    private Integer crsTotlRqrmMin; // 소요시간 (단위: 분)

    @Column
    private Integer crsLevel; // 난이도 (1~3 등급)

    @Lob
    @Column
    private String crsSummary; // 요약 설명

    @Lob
    @Column
    private String crsTourInfo; // 관광 정보

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

//    @Lob
//    @Column
//    private String crsContents; // 상세 설명

//    @Lob
//    @Column
//    private String travelerInfo; // 여행자 정보

//    @Column
//    private String sigun; // 지역

//    @Column
//    private String spotId;

//    @Column
//    private String type;

    @ManyToOne
    @JoinColumn(name = "themeId")
    private Theme theme;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private List<Run> runList = new ArrayList<>();

    public static Course of(String courseId, String name, Double distance, Integer duration, Integer level,
                            String summary, String tourInfo, String gpx, String created, String modified) {
        Course course = new Course();
        course.setCourseId(courseId);
        course.setCrsKorNm(name);
        course.setCrsDstnc(distance);
        course.setCrsTotlRqrmMin(duration);
        course.setCrsLevel(level);
        course.setCrsSummary(summary);
        course.setCrsTourInfo(tourInfo);
        course.setGpxpath(gpx);
        course.setCreatedtime(created);
        course.setModifiedtime(modified);
        return course;
    }
}
