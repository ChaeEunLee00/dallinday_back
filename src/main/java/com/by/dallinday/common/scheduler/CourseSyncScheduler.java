package com.by.dallinday.common.scheduler;

import com.by.dallinday.course.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseSyncScheduler {
    private final CourseService courseService;

    @Scheduled(cron = "0 0 3 * * *") // 초 분 시 일 월 요일 => 매일 새벽 3시
    public void scheduledSync() {
        log.info("[동기화 시작] 현재 시각: " + LocalDateTime.now());
        courseService.syncCourses();
        log.info("[동기화 완료] 현재 시각: " + LocalDateTime.now());
    }
}
