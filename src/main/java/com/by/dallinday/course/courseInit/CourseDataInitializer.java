package com.by.dallinday.course.courseInit;

import com.by.dallinday.course.Course;
import com.by.dallinday.course.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseDataInitializer implements CommandLineRunner {

    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {
        courseRepository.save(Course.of(
                "COURSE001", "아산로 코스", 2.51, 16, 2,
                "바다를 보며 걷기 좋은 산책로입니다.",
                "장소1-장소2-장소3-장소4",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/%E1%84%8B%E1%85%A1%E1%84%89%E1%85%A1%E1%86%AB%E1%84%85%E1%85%A9.gpx",
                "20250807120000", "20250807120000"
        ));

        courseRepository.save(Course.of(
                "COURSE002", "문수구장 코스", 2.7, 29, 1,
                "숲속을 걷는 힐링 코스입니다.",
                "장소1-장소2-장소3-장소4",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/%E1%84%86%E1%85%AE%E1%86%AB%E1%84%89%E1%85%AE%E1%84%80%E1%85%AE%E1%84%8C%E1%85%A1%E1%86%BC.gpx",
                "20250807121000", "20250807121000"
        ));

        courseRepository.save(Course.of(
                "COURSE003", "울산대공원 코스", 2.6, 49, 1,
                "도심 경관과 문화를 함께 즐길 수 있는 코스입니다.",
                "장소1-장소2-장소3-장소4",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/%E1%84%8B%E1%85%AE%E1%86%AF%E1%84%89%E1%85%A1%E1%86%AB%E1%84%83%E1%85%A2%E1%84%80%E1%85%A9%E1%86%BC%E1%84%8B%E1%85%AF%E1%86%AB.gpx",
                "20250807122000", "20250807122000"
        ));

        courseRepository.save(Course.of(
                "COURSE004", "태화강 코스", 40.2, 300, 3,
                "경사가 있는 본격적인 산악 트레킹 코스입니다.",
                "장소1-장소2-장소3-장소4",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/%E1%84%90%E1%85%A2%E1%84%92%E1%85%AA%E1%84%80%E1%85%A1%E1%86%BC3%E1%84%89%E1%85%B5%E1%84%80%E1%85%A1%E1%86%AB.gpx",
                "20250807123000", "20250807123000"
        ));

        courseRepository.save(Course.of(
                "COURSE005", "등대 코스", 1.35, 6, 2,
                "강을 따라 자전거를 탈 수 있는 코스입니다.",
                "장소1-장소2-장소3-장소4",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/%E1%84%83%E1%85%B3%E1%86%BC%E1%84%83%E1%85%A2.gpx",
                "20250807124000", "20250807124000"
        ));

        log.info("Course Initialization done: course count = {}", courseRepository.count());
    }
}
