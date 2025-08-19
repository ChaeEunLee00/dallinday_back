package com.by.dallinday.ranking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingResponse {
    private Long memberId;

    private String username;

    private Long monthlyRank;

    private Double monthlyDistance;
}
