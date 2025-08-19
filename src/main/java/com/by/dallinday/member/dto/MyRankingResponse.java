package com.by.dallinday.member.dto;

import com.by.dallinday.ranking.dto.RankingResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyRankingResponse {
    private String yearMonth;
    private Long monthlyTotalNum;
    private RankingResponse my;
    private RankingResponse prev;
    private RankingResponse next;
}
