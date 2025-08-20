package com.by.dallinday.member;

import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.member.dto.MemberGetResponse;
import com.by.dallinday.member.dto.MyPageGetResponse;
import com.by.dallinday.member.dto.MyRankingDetailResponse;
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
        MyPageGetResponse response = memberService.findMyPage(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 찜 코스 리스트 조회
    @GetMapping("/{member-id}/favorites")
    public ResponseEntity getFavorites(@PathVariable("member-id") @Positive Long memberId) {

        List<CourseListResponse> response = memberService.findFavorites(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 멤버 탈퇴
    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive Long pathMemberId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        Long tokenMemberId = Long.valueOf(principal.get("memberId").toString());

        if (!pathMemberId.equals(tokenMemberId)) {
            return new ResponseEntity<>("본인만 탈퇴할 수 있습니다.", HttpStatus.FORBIDDEN);
        }

        memberService.removeMember(pathMemberId);
        return new ResponseEntity<>("탈퇴가 완료되었습니다.", HttpStatus.OK);
    }

    // 순위 상세 조회
    @GetMapping("/{member-id}/mypage/ranking")
    public ResponseEntity getMyRanking(@PathVariable("member-id") @Positive Long memberId) {

        // 개인 기록, 순위, 뱃지, 달리기 리스트 전달
        MyRankingDetailResponse response = memberService.findMyRanking(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
