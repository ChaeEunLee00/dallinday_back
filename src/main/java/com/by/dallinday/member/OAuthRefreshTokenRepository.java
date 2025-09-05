package com.by.dallinday.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthRefreshTokenRepository extends JpaRepository<OAuthRefreshToken, Long> {
    Optional<OAuthRefreshToken> findByMember_MemberId(Long memberId);
}
