package com.by.dallinday.course;

import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.course.dto.CourseResponse;
import com.by.dallinday.course.dto.CourseTop5Response;
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
    public ResponseEntity getCourse(@PathVariable("course-id") Long courseId) {

        CourseResponse response = courseService.findCourse(courseId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 관광지 별 코스 리스트 조회
    @GetMapping("/spot/{spot-id}")
    public ResponseEntity getSpotCourses(@PathVariable("spot-id") @Positive Long spotId,
                                         @RequestParam(defaultValue = "distance") String sortBy) {

        List<CourseListResponse> response = courseService.findSpotCourseList(spotId, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 인기 코스 top5 리스트 조회
    @GetMapping("/top5")
    public ResponseEntity getTop5Courses() {

        List<CourseTop5Response> response = courseService.findTop5CourseList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 찜 생성
    @PostMapping("/{course-id}/favorite")
    public ResponseEntity postCourseFavorite(@PathVariable("course-id") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        Long memberId = Long.valueOf(principal.get("memberId").toString());

        courseService.createCourseFavorite(courseId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 찜 취소
    @DeleteMapping("/{course-id}/favorite")
    public ResponseEntity deleteCourseFavorite(@PathVariable("course-id") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        Long memberId = Long.valueOf(principal.get("memberId").toString());

        courseService.removeCourseFavorite(courseId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
