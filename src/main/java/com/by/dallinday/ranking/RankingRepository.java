package com.by.dallinday.ranking;

import com.by.dallinday.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    Optional<Ranking> findByMemberAndYearMonth(Member member, String yearMonth);

    List<Ranking> findByYearMonthOrderByMonthlyTotalDistanceDesc(String yearMonth);

    List<Ranking> findByYearMonthOrderByMonthlyRank(String yearMonth);
}
