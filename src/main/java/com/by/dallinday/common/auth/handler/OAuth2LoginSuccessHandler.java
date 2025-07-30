package com.by.dallinday.common.auth.handler;

import com.by.dallinday.common.auth.jwt.JwtTokenizer;
import com.by.dallinday.common.auth.service.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        String refreshToken = JwtTokenizer.generateRefreshToken(base64EncodedSecretKey);

        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        // Access Token, Refresh Token 헤더 설정
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);

        // 응답 상태코드
        response.setStatus(HttpStatus.OK.value());

//        // 로그인 성공 후 리다이렉트 주소 -> 프론트 주소
//        response.sendRedirect(createURI(accessToken, refreshToken));
    }

//    private String createURI(String accessToken, String refreshToken) {
//        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
//        queryParams.add("access_token", accessToken);
//        queryParams.add("refresh_token", refreshToken);
//
//        return UriComponentsBuilder.newInstance()
//                .scheme("https")
//                .host("dallinday.com")
// //                .port(3000)
//                .path("/oauth")
//                .queryParams(queryParams)
//                .encode(StandardCharsets.UTF_8)
//                .build()
//                .toUriString();
//    }
}
