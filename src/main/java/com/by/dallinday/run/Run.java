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
    private Long distance;

    @Column
    private Long duration;

    @Column
    private String review;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;
}