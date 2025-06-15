package com.by.dallinday.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
//    private final MemberService memberService;

    // 마이 페이지 조회
    @GetMapping("/{member-id}")
    public ResponseEntity getMyPage() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 개인 기록 조회
    @GetMapping("/{member-id}/record")
    public ResponseEntity getMyRecord() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 순위 조회
    @GetMapping("/{member-id}/ranking")
    public ResponseEntity getMyRanking() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 나의 뱃지 리스트 조회
    @GetMapping("/{member-id}/badges")
    public ResponseEntity getMyBadges() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 나의 달리기 리스트 조회
    @GetMapping("/{member-id}/runs")
    public ResponseEntity getMyCourses() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
