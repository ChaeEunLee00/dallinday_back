package com.by.dallinday.common.auth.admin;

import com.by.dallinday.common.auth.util.UriUtil;
import com.by.dallinday.course.Course;
import com.by.dallinday.course.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final CourseService courseService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {
        LoginResponse response = adminService.login(request);

        // 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + response.getAccessToken());
        headers.add("Refresh", response.getRefreshToken());
        headers.add(HttpHeaders.LOCATION, UriUtil.buildMyAppRedirectUri(response.getAccessToken(), response.getRefreshToken(), response.getMemberId()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .build();
    }

    // 코스 생성 (관리자용)
    @PostMapping("/courses")
    public ResponseEntity postCourse(@RequestBody Course request) {

        Course response = courseService.createCourse(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 코스 수정 (관리자용)
    @PatchMapping("/courses/{course-id}")
    public ResponseEntity<Course> patchCourse(
            @PathVariable("course-id") String courseId,
            @RequestBody Course request) { // 수정하고싶은 필드만 작성하여 전송

        Course response = courseService.updateCourse(courseId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 코스 삭제 (관리자용)
    @DeleteMapping("/courses/{course-id}")
    public ResponseEntity deleteCourse(@PathVariable("course-id") String courseId) {

        courseService.removeCourse(courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 코스 데이터 동기화 요청
    @PostMapping("/courses/sync")
    public ResponseEntity syncCourses() {
        courseService.syncCourses();
        return new ResponseEntity<>("Course data synced", HttpStatus.OK);
    }
}
