package com.by.dallinday.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageGetResponse {

    private Long memberId;

    private String username;

    private Double totalDistance;

    private Double totalDuration;

    private Double avgPace;

    private String badges;

    private MyRankingResponse ranking;
}
