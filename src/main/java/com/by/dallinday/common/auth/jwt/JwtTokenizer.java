package com.by.dallinday.common.auth.jwt;

import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenizer {

    @Getter
    @Value("${jwt.key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    @Value("${jwt.access.header}")
    @Getter
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    @Getter
    private String refreshHeader;

    private final MemberService memberService;


    public String encodedBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Key getKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }

    //엑세스 토큰 생성
    public String generateAccessToken(Long memberId, String base64EncodedSecretKey) {
        Key key = getKey(base64EncodedSecretKey);
        log.info("[generateToken] accessToken 생성");

        Date expiration = getExpiration(accessTokenExpirationMinutes); // 만료시간
        Member foundMember = memberService.findMember(memberId); // 사용자 정보 조회

        return Jwts.builder()
                .setSubject(foundMember.getEmail())
                .claim("memberId", foundMember.getMemberId())
                .claim("email", foundMember.getEmail())
                .claim("username", foundMember.getUsername())
                .claim("role", foundMember.getRole())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    //리프레쉬 토큰 생성
    public String generateRefreshToken(String base64EncodedSecretKey) {
        Key key = getKey(base64EncodedSecretKey);
        log.info("[generateToken] refreshToken 생성");

        Date expiration = getExpiration(refreshTokenExpirationMinutes);
        return Jwts.builder()
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKey(base64EncodedSecretKey);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(jws);
        return claims;
    }

    public Date getExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();
        return expiration;
    }
}
