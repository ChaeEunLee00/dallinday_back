package com.by.dallinday.run;

import com.by.dallinday.course.Course;
import com.by.dallinday.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "runs")
public class Run {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long runId;

    @Column
    private Double distance;

    @Column
    private Double duration;

    @Column
    private Double pace;

    @Column
    private Double calorie;

    @Column
    private Double accuracy;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    // 실제 지나간 관광지 목록..?

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;
}