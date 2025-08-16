package com.by.dallinday.common.auth.admin;

import com.by.dallinday.common.auth.util.UriUtil;
import com.by.dallinday.course.Course;
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

        Course response = adminService.createCourse(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 코스 수정 (관리자용)
    @PatchMapping("/courses/{course-id}")
    public ResponseEntity<Course> patchCourse(
            @PathVariable("course-id") String courseId,
            @RequestBody Course request) { // 수정하고싶은 필드만 작성하여 전송

        Course response = adminService.updateCourse(courseId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 코스 삭제 (관리자용)
    @DeleteMapping("/courses/{course-id}")
    public ResponseEntity deleteCourse(@PathVariable("course-id") String courseId) {

        adminService.removeCourse(courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
