package com.by.dallinday.ranking;

import com.by.dallinday.ranking.dto.RankingHistoryResponse;
import com.by.dallinday.ranking.dto.RankingResponse;
import org.springframework.stereotype.Component;

@Component
public class RankingMapper {
    public RankingResponse rankingToRankingResponse(Ranking ranking){
        if ( ranking == null ) {
            return null;
        }

        RankingResponse rankingResponse = new RankingResponse();

        rankingResponse.setMemberId(ranking.getMember().getMemberId());
        rankingResponse.setUsername(ranking.getMember().getUsername());
        rankingResponse.setMonthlyRank(ranking.getMonthlyRank());
        rankingResponse.setMonthlyDistance(ranking.getMonthlyTotalDistance());

        return rankingResponse;
    }

    public RankingHistoryResponse rankingToRankingHistroyResponse(Ranking ranking){
        if ( ranking == null ) {
            return null;
        }

        RankingHistoryResponse rankingHistoryResponse = new RankingHistoryResponse();

        rankingHistoryResponse.setYearMonth(ranking.getRankingMonth());
        rankingHistoryResponse.setMonthlyRank(ranking.getMonthlyRank());

        return rankingHistoryResponse;
    }
}
