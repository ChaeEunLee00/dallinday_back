package com.by.dallinday.common.auth.jwt;

import com.by.dallinday.member.MemberService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshService {

    private final JwtTokenizer jwtTokenizer;

    public String createNewAccessToken(String refreshToken){
        // 1. Refresh Token에서 memberId 추출 (토큰 검증 포함)
        String base64EncodedSecretKey = jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(refreshToken, base64EncodedSecretKey).getBody();
        Long memberId = Long.valueOf(claims.get("memberId").toString());

        // 2. memberId를 기반으로 새 Access Token 생성
        String accessToken = jwtTokenizer.generateAccessToken(memberId, base64EncodedSecretKey);

        log.info("memberId : {}", memberId);
        log.info("accessToken : {}", accessToken);

        return accessToken;
    }
}
