package com.by.dallinday.course;

import com.by.dallinday.coursespot.CourseSpot;
import com.by.dallinday.run.Run;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column
    private String name; // 코스명

    @Lob
    @Column
    private String description; // 요약 설명

    @Column
    private Double distance; // 거리 (단위: km)

    @Column
    private Integer duration; // 소요시간 (단위: 분)

    @Column
    private Integer difficulty; // 난이도 (1~3 등급)

    @Column
    private String gpxpath; // GPX 파일 경로

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    @OrderBy("orderIndex ASC")
    private List<CourseSpot> courseSpotList; // 지나는 관광지 목록

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private List<Run> runList = new ArrayList<>();

    @Column
    private LocalDateTime createdAt; // 생성일시

    @Column
    private LocalDateTime modifiedAt; // 수정일시

    public static Course of(String name, String description, String gpxpath) {
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        course.setGpxpath(gpxpath);
        return course;
    }
}
