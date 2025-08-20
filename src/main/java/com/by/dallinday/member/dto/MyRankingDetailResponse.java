package com.by.dallinday.member.dto;

import com.by.dallinday.ranking.dto.RankingHistoryResponse;
import com.by.dallinday.ranking.dto.RankingResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyRankingDetailResponse {
    private String yearMonth;
    private Long monthlyTotalNum;
    private RankingResponse my;
    private RankingResponse prev;
    private RankingResponse next;
    private List<RankingHistoryResponse> rankingHistory;
    private List<RankingResponse> totalRanking;
}
