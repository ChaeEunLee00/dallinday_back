package com.by.dallinday.rank;

import com.by.dallinday.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank, Long> {
    Optional<Rank> findByMemberAndYearMonth(Member member, String yearMonth);

    List<Rank> findByYearMonthOrderByTotalDistanceDesc(String yearMonth);
}
