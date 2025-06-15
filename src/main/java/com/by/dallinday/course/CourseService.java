package com.by.dallinday.course;

import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // 코스 조회
    public Course findCourse() {
        return new Course();
    }

    // 관광지 별 코스 리스트 조회
    public Course findSpotCourseList() {
        return new Course();
    }

    // 테마 별 코스 리스트 조회
    public Course findThemeCourseList() {
        return new Course();
    }
}
