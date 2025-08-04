package com.by.dallinday.common.auth.oauth;

import com.by.dallinday.common.auth.jwt.JwtTokenizer;
import com.by.dallinday.common.auth.util.UriUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenizer JwtTokenizer;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("Oauth 로그인 성공");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        loginSuccess(response, oAuth2User);
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String base64EncodedSecretKey = JwtTokenizer.encodedBase64SecretKey(JwtTokenizer.getSecretKey());
        String accessToken = JwtTokenizer.generateAccessToken(oAuth2User.getMemberId(), base64EncodedSecretKey);
        String refreshToken = JwtTokenizer.generateRefreshToken(oAuth2User.getMemberId(), base64EncodedSecretKey);

        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        // Access Token, Refresh Token 헤더 설정
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);

        // 응답 상태코드
        response.setStatus(HttpStatus.OK.value());

        // 로그인 성공 후 리다이렉트 주소 -> 프론트 주소
        // 크롬에서 벗어나 프론트로 돌아가기 위함
        response.sendRedirect(UriUtil.buildMyAppRedirectUri(accessToken, refreshToken, oAuth2User.getMemberId()));
    }
}
