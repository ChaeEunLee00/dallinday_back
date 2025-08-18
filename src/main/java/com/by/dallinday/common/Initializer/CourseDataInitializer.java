package com.by.dallinday.common.Initializer;

import com.by.dallinday.course.Course;
import com.by.dallinday.course.CourseRepository;
import com.by.dallinday.course.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev") // 개발 환경에서만 실행
@RequiredArgsConstructor
public class CourseDataInitializer implements CommandLineRunner {

    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {
        courseService.createCourse(Course.of(
                "아산로 코스",
                "바다를 보며 걷기 좋은 산책로입니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/%E1%84%8B%E1%85%A1%E1%84%89%E1%85%A1%E1%86%AB%E1%84%85%E1%85%A9.gpx"
        ));

        courseService.createCourse(Course.of(
                "문수구장 코스",
                "숲속을 걷는 힐링 코스입니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/%E1%84%86%E1%85%AE%E1%86%AB%E1%84%89%E1%85%AE%E1%84%80%E1%85%AE%E1%84%8C%E1%85%A1%E1%86%BC.gpx"
        ));

        courseService.createCourse(Course.of(
                "울산대공원 코스",
                "도심 경관과 문화를 함께 즐길 수 있는 코스입니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/%E1%84%8B%E1%85%AE%E1%86%AF%E1%84%89%E1%85%A1%E1%86%AB%E1%84%83%E1%85%A2%E1%84%80%E1%85%A9%E1%86%BC%E1%84%8B%E1%85%AF%E1%86%AB.gpx"
        ));

        courseService.createCourse(Course.of(
                "태화강 코스",
                "경사가 있는 본격적인 산악 트레킹 코스입니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/%E1%84%90%E1%85%A2%E1%84%92%E1%85%AA%E1%84%80%E1%85%A1%E1%86%BC3%E1%84%89%E1%85%B5%E1%84%80%E1%85%A1%E1%86%AB.gpx"
        ));

        courseService.createCourse(Course.of(
                "등대 코스",
                "강을 따라 자전거를 탈 수 있는 코스입니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/%E1%84%83%E1%85%B3%E1%86%BC%E1%84%83%E1%85%A2.gpx"
        ));

        log.info("Course Initialization done: course count = {}", courseRepository.count());
    }
}
