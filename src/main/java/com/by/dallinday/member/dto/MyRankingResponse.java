package com.by.dallinday.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyRankingResponse {
    private Long totalNum;
    private RankingResponse my;
    private RankingResponse prev;
    private RankingResponse next;
}
