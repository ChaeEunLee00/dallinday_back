package com.by.dallinday.course;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CourseController {
    private final CourseService courseService;

    // 코스 조회
    @GetMapping("/courses/{course-id}")
    public ResponseEntity getCourse() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 관광지 별 코스 리스트 조회
    @GetMapping("/spots/{spot-id}")
    public ResponseEntity getSpotCourses() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 테마 별 코스 리스트 조회
    @GetMapping("/themes/{theme-id}")
    public ResponseEntity getThemeCourses() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
