package com.by.dallinday.ranking;

import com.by.dallinday.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "rankings")
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankId;

    @Column
    private String yearMonth; // YYYY-MM 형태로 저장 (문자열) e.g. "2025-08"

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column
    private Double monthlyTotalDistance = 0.0; // 월 누적 km

    @Column
    private Long monthlyRank; // 월별 랭킹(1위부터, 동점은 같은 랭크 = Dense Rank)

    @Column
    private LocalDateTime updatedAt;
}
