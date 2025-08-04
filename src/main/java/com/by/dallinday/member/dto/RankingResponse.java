package com.by.dallinday.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingResponse {
    private Long memberId;

    private String username;

    private Long ranking;

    private Double totalDistance;
}
