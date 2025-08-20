package com.by.dallinday.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberResponse {
    private Long memberId;

    private String email;

    private String username;

    private String provider;

    private String providerId;

    private String imageUrl;

    private String role;

    private LocalDateTime createdAt;
}
