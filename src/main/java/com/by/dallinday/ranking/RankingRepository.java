package com.by.dallinday.ranking;

import com.by.dallinday.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    Optional<Ranking> findByMemberAndRankingMonth(Member member, String rankingMonth);

    List<Ranking> findByRankingMonthOrderByMonthlyTotalDistanceDesc(String rankingMonth);

    List<Ranking> findByRankingMonthOrderByMonthlyRank(String rankingMonth);

    List<Ranking> findByMember_MemberIdOrderByRankingMonthDesc(Long memberId);
}
