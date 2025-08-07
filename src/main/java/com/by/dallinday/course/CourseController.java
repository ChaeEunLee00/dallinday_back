package com.by.dallinday.course;

import com.by.dallinday.course.dto.CourseListResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    // 코스 조회
    @GetMapping("/{course-id}")
    public ResponseEntity getCourse() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 관광지 별 코스 리스트 조회
    @GetMapping("/spot/{spot-id}")
    public ResponseEntity getSpotCourses(@PathVariable("spot-id") @Positive Long spotId) {

        List<CourseListResponse> response = courseService.findSpotCourseList(spotId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 테마 별 코스 리스트 조회
    @GetMapping("/theme/{theme-id}")
    public ResponseEntity getThemeCourses() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 코스 데이터 동기화 요청
    @PostMapping("/sync")
    public ResponseEntity syncCourses() {
        courseService.syncCourses();
        return new ResponseEntity<>("Course data synced", HttpStatus.OK);
    }
}
