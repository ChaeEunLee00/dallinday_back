package com.by.dallinday.member;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.member.dto.MemberGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    // 마이 페이지 조회
    public Member findMyPage() {
        return new Member();
    }

    // 개인 기록 조회
    public Member findMyRecord() {
        return new Member();
    }

    // 순위 조회
    public Member findMyRanking() {
        return new Member();
    }

    // 나의 뱃지 리스트 조회
    public Member findMyBadges() {
        return new Member();
    }

    // 나의 달리기 리스트 조회
    public Member findMyRuns() {
        return new Member();
    }

    // 멤버 조회
    public MemberGetResponse findMember(Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return memberMapper.memberToMemberGetResponse(member);
    }
}
