package com.by.dallinday.common.auth.oauth;

import com.by.dallinday.common.auth.jwt.JwtTokenizer;
import com.by.dallinday.common.auth.util.EncryptUtil;
import com.by.dallinday.common.auth.util.UriUtil;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import com.by.dallinday.member.OAuthRefreshToken;
import com.by.dallinday.member.OAuthRefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final EncryptUtil encryptUtil;
    private final JwtTokenizer JwtTokenizer;
    private final MemberRepository memberRepository;
    private final OAuthRefreshTokenRepository oAuthRefreshTokenRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("Oauth 로그인 성공");
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        storeOAuthRefreshToken(authentication, oAuth2User.getMemberId());
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

    private void storeOAuthRefreshToken(Authentication authentication, Long memberId) {
        if (!(authentication instanceof OAuth2AuthenticationToken token)) return;
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());

        if (client == null) return;
        OAuth2RefreshToken providerRt = client.getRefreshToken();
        if (providerRt == null) return;
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) return;

        OAuthRefreshToken oAuthRefreshToken = oAuthRefreshTokenRepository.findByMember(member)
                .orElseGet(OAuthRefreshToken::new);

        oAuthRefreshToken.setMember(member);
        oAuthRefreshToken.setEncryptedRefreshToken(encryptUtil.encrypt(providerRt.getTokenValue()));

        oAuthRefreshTokenRepository.save(oAuthRefreshToken);
    }
}
