package com.by.dallinday.member;

import com.by.dallinday.member.dto.MemberGetResponse;
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

    public MemberGetResponse memberToMemberGetResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberGetResponse memberGetResponse = new MemberGetResponse();

        memberGetResponse.setMemberId(member.getMemberId());
        memberGetResponse.setEmail(member.getEmail());
        memberGetResponse.setUsername(member.getUsername());
        memberGetResponse.setProvider(member.getProvider());
        memberGetResponse.setProviderId(member.getProviderId());
        memberGetResponse.setImageUrl(member.getImageUrl());
        memberGetResponse.setRole(member.getRole());
        memberGetResponse.setCreatedAt(member.getCreatedAt());

        return memberGetResponse;
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
