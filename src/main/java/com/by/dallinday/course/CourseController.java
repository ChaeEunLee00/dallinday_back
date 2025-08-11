package com.by.dallinday.course;

import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.course.dto.CourseResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    // 코스 조회
    @GetMapping("/{course-id}")
    public ResponseEntity getCourse(@PathVariable("course-id") String courseId) {

        CourseResponse response = courseService.findCourse(courseId);
        return new ResponseEntity<>(response, HttpStatus.OK);
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

    @PostMapping("/{courseId}/favorite")
    public ResponseEntity postCourseFavorite(@PathVariable("course-id") String courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        Long memberId = Long.valueOf(principal.get("memberId").toString());

        courseService.createCourseFavorite(courseId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}/like")
    public ResponseEntity deleteCourseFavorite(@PathVariable("course-id") String courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        Long memberId = Long.valueOf(principal.get("memberId").toString());

        courseService.removeCourseFavorite(courseId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 코스 데이터 동기화 요청
    @PostMapping("/sync")
    public ResponseEntity syncCourses() {
        courseService.syncCourses();
        return new ResponseEntity<>("Course data synced", HttpStatus.OK);
    }
}
