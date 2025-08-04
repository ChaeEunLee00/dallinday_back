package com.by.dallinday.member.dto;

import com.by.dallinday.run.dto.RunResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    private List<RunResponse> runList = new ArrayList<>();
}
