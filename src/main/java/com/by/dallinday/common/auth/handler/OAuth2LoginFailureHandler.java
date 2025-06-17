package com.by.dallinday.common.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String errorMessage = exception.getMessage();

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.sendRedirect(createURI(errorMessage));
        log.info("소셜 로그인에 실패했습니다. 에러 머세지 : {}", errorMessage);
    }

    private String createURI(String errorMessage) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("dallinday.com")
//                .port(3000)
                .path("/oauth")
                .queryParam("error", errorMessage)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();
    }
}
