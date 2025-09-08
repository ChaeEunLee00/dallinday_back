package com.by.dallinday.member;

import com.by.dallinday.common.auth.oauth.refresh.OAuthRefreshToken;
import com.by.dallinday.favorite.Favorite;
import com.by.dallinday.ranking.Ranking;
import com.by.dallinday.run.Run;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @Column
    private String imageUrl;

    @Column
    private String role;

    @Column
    private LocalDateTime createdAt;

    @Column
    private Double totalDistance = 0.0;

    @Column
    private Double totalDuration = 0.0;

    @Column
    private Double avgPace = 0.0;

    @Column
    private String badges = "100000000";

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("startTime DESC")
    private List<Run> runList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Favorite> favoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Ranking> rankingList = new ArrayList<>(); // cascade all 삭제용으로 임시 생성

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private OAuthRefreshToken oAuthRefreshToken;
}
