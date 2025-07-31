package com.by.dallinday.common.auth.jwt;

import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/refresh")
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshService refreshService;

    @PostMapping
    public ResponseEntity postNewAccessToken(HttpServletRequest request) {
        try {
            // Refresh Token을 사용해 새 Access Token 생성
            String refreshToken = request.getHeader("Refresh");
            String accessToken = refreshService.createNewAccessToken(refreshToken);

            // 헤더 세팅
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .build();

        } catch (JwtException e) {
            // Refresh Token 오류 처리
            log.info("error Message : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
