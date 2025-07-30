package com.by.dallinday.common.auth.admin;

import com.by.dallinday.common.auth.jwt.JwtTokenizer;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request) {
        Optional<Member> foundMember = memberRepository.findByEmail(request.getId());

        // 아이디 불일치
        if(foundMember.isEmpty()){
            log.info("Login failed - ID not found: {}", request.getId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 불일치
        Member member = foundMember.get();
        if (!passwordEncoder.matches(request.getPassword(), member.getProviderId())) {
            log.info("Login failed - Incorrect password for ID: {}", request.getId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 관리자 로그인 성공 - JWT 생성
        String base64EncodedSecretKey = jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(member.getMemberId(), base64EncodedSecretKey);
        String refreshToken = jwtTokenizer.generateRefreshToken(base64EncodedSecretKey);

        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        // 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Refresh-Token", refreshToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .build();
    }
}
