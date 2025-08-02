package com.by.dallinday.member;

import com.by.dallinday.member.dto.MemberGetResponse;
import com.by.dallinday.member.dto.MyPageGetResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    // 멤버 조회
    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive Long memberId) {

        MemberGetResponse response = memberService.findMember(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 마이 페이지 조회
    @GetMapping("/{member-id}/mypage")
    public ResponseEntity getMyPage(@PathVariable("member-id") @Positive Long memberId) {

        // 개인 기록, 순위, 뱃지, 달리기 리스트 전달
        System.out.println(1);
        MyPageGetResponse response = memberService.findMyPage(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 순위 상세 조회
    @GetMapping("/{member-id}/mypage/ranking")
    public ResponseEntity getMyRanking() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 개인 기록 상세 조회
    @GetMapping("/{member-id}/mypage/record")
    public ResponseEntity getMyRecord() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
