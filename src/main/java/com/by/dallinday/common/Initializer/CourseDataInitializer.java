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
                "선바위–포항고속도로 코스",
                "선바위에서 출발해 포항고속도로 인근까지 이어지는 장거리 코스로, 도시와 자연 풍경을 함께 경험할 수 있습니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE001.gpx"
        ));

        courseService.createCourse(Course.of(
                "UNIST 캠퍼스 큰 한바퀴 코스",
                "UNIST 캠퍼스를 크게 한 바퀴 도는 장거리 루프 코스로, 교내의 다양한 풍경을 즐기며 달릴 수 있습니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE002.gpx"
        ));

        courseService.createCourse(Course.of(
                "UNIST 캠퍼스 작은 한바퀴 코스",
                "UNIST 캠퍼스를 가볍게 한 바퀴 도는 코스로, 학생들이 일상 속에서 쉽게 달릴 수 있습니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE003.gpx"
        ));

        courseService.createCourse(Course.of(
                "대나무숲 코스",
                "태화강 국가정원 대나무숲을 가로지르는 코스로, 시원한 숲길과 강변 풍경을 동시에 즐길 수 있습니다. 여름철에도 그늘이 많아 쾌적하게 달리기 좋은 코스입니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE004.gpx"
        ));

        courseService.createCourse(Course.of(
                "대공원 남문–동문 코스",
                "울산대공원의 남문에서 동문으로 이어지는 산책로 코스로, 공원의 울창한 숲과 함께 달릴 수 있습니다. 잘 정비된 길로 초보 러너부터 가족 러닝에도 적합합니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE005.gpx"
        ));

        courseService.createCourse(Course.of(
                "선암호수공원 코스",
                "울산 시민들이 즐겨 찾는 호수공원을 한 바퀴 도는 코스로, 물가와 숲길을 동시에 즐길 수 있습니다. 평탄하고 짧은 거리라 가벼운 조깅이나 산책 러닝에 적합합니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE006.gpx"
        ));

        courseService.createCourse(Course.of(
                "옥교동 코스",
                "울산 도심 옥교동을 출발해 다양한 거리를 달릴 수 있는 러닝 루트입니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE007.gpx"
        ));

        courseService.createCourse(Course.of(
                "동천 합수목 코스",
                "울산 동천강을 따라 체육관에서 합수목까지 이어지는 강변 러닝 코스입니다. 평탄하고 달리기 좋은 길로, 아침 러닝이나 퇴근 후 가벼운 운동에 잘 어울립니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE008.gpx"
        ));

        courseService.createCourse(Course.of(
                "외솔교–동천교 강변 코스",
                "울산 도심을 가로지르는 동천강을 따라 이어지는 긴 강변 코스입니다. 탁 트인 강변 풍경과 함께 장거리 러닝 훈련에 적합합니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE009.gpx"
        ));

        courseService.createCourse(Course.of(
                "염포산 숲길 코스",
                "울산항을 내려다보며 숲길을 달릴 수 있는 염포산 트레일 러닝 코스입니다. 비교적 부담 없는 거리로 가볍게 자연을 즐기기에 좋습니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE010.gpx"
        ));

        courseService.createCourse(Course.of(
                "일산지 해변 코스",
                "울산의 대표적인 해변인 일산지를 따라 달리는 코스로, 바다 풍경과 함께 시원한 러닝을 즐길 수 있습니다. 비교적 짧고 평탄해 누구나 부담 없이 뛰기 좋습니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE011.gpx"
        ));

        courseService.createCourse(Course.of(
                "등대 해안 코스",
                "푸른 바다와 등대를 따라 이어지는 해안 코스로, 시원한 바닷바람을 맞으며 달릴 수 있습니다. 코스 길이가 적당하여 아침 러닝이나 가벼운 산책 러닝에 좋습니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE012.gpx"
        ));

        courseService.createCourse(Course.of(
                "간절곶 바닷길 코스",
                "바다를 옆에 두고 달리는 왕복 코스로, 시원한 바닷바람과 파도 소리를 즐길 수 있습니다. 특히 아침이나 저녁 시간대에 달리면 환상적인 일출·일몰 풍경을 만날 수 있습니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE013.gpx"
        ));

        courseService.createCourse(Course.of(
                "동천강 코스",
                "동천강을 따라 이어지는 강변 산책로로, 도심 속에서도 자연을 느낄 수 있는 코스입니다. 평탄한 길이 많아 초보자부터 꾸준히 달리는 러너까지 모두 즐기기 좋습니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE014.gpx"
        ));

        courseService.createCourse(Course.of(
                "간월재 더블 피크 코스",
                "울산 간월재를 중심으로 두 봉우리를 잇는 산악 러닝 코스입니다. 탁 트인 억새밭과 능선을 따라 달리며 하늘과 맞닿은 듯한 풍경을 만끽할 수 있습니다.",
                "https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/courses/COURSE015.gpx"
        ));

        log.info("Course Initialization done: course count = {}", courseRepository.count());
    }
}
