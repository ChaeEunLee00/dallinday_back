package com.by.dallinday.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseAPIClient courseAPIClient;
    private final CourseRepository courseRepository;

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

    // TourAPI를 통해 가져온 코스 데이터 저장 및 동기화
    public void syncCourses() {
        int page = 1, size = 10; // 가져올 데이터 수
        List<CourseItem> courseItems = courseAPIClient.callCourseAPI(size, page);

        for (CourseItem item : courseItems) {
            // 이미 존재하는 코스인지 확인
            Optional<Course> foundCourse = courseRepository.findById(item.getCourseId());
            if (foundCourse.isPresent()) {
                // 이미 있다면 업데이트
                Course course = foundCourse.get();
                course.updateFrom(item);
            } else {
                // 없으면 새로 저장
                Course newCourse = Course.from(item);
                courseRepository.save(newCourse);
            }
        }
    }
}
