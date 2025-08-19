package com.by.dallinday.coursespot;

import com.by.dallinday.course.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "course_spots")
public class CourseSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // default = EAGER
    @JoinColumn(name = "courseId")
    private Course course;

    @Column(nullable = false)
    private Long spotId;

    @Column
    private String name;

    @Column
    private Double longitude;  // 경도

    @Column
    private Double latitude;  // 위도

    @Column(nullable = false)
    private Integer orderIndex;
}
