package com.by.dallinday.member;

import com.by.dallinday.member.dto.MemberGetResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

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
}
