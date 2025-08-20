package com.by.dallinday.ranking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingHistoryResponse {
    private String yearMonth;
    private Long monthlyRank;
}
