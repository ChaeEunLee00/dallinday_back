package com.by.dallinday.member;

import com.by.dallinday.member.dto.MemberResponse;
import com.by.dallinday.member.dto.MyPageGetResponse;
import com.by.dallinday.run.RunMapper;
import com.by.dallinday.run.dto.RunResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    private final RunMapper runMapper;

    public MemberResponse memberToMemberResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberResponse memberResponse = new MemberResponse();

        memberResponse.setMemberId(member.getMemberId());
        memberResponse.setEmail(member.getEmail());
        memberResponse.setUsername(member.getUsername());
        memberResponse.setProvider(member.getProvider());
        memberResponse.setProviderId(member.getProviderId());
        memberResponse.setImageUrl(member.getImageUrl());
        memberResponse.setRole(member.getRole());
        memberResponse.setCreatedAt(member.getCreatedAt());

        return memberResponse;
    }

    public MyPageGetResponse memberToMyPageGetResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        MyPageGetResponse myPageGetResponse = new MyPageGetResponse();

        myPageGetResponse.setMemberId(member.getMemberId());
        myPageGetResponse.setUsername(member.getUsername());
        myPageGetResponse.setTotalDistance(member.getTotalDistance());
        myPageGetResponse.setTotalDuration(member.getTotalDuration());
        myPageGetResponse.setAvgPace(member.getAvgPace());
        myPageGetResponse.setBadges(member.getBadges());

        List<RunResponse> runResponseList = member.getRunList().stream()
                .map(run -> runMapper.runToRunResponse(run))
                .toList();

        myPageGetResponse.setRunList(runResponseList);

        return myPageGetResponse;
    }
}
