package com.by.dallinday.common.auth.admin;

import com.by.dallinday.common.auth.jwt.JwtTokenizer;
import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.course.CourseRepository;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    // 관리자 로그인
    public LoginResponse login(LoginRequest request) {
        // 아이디 불일치
        Member member = memberRepository.findByEmail(request.getId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // 비밀번호 불일치
        if (!passwordEncoder.matches(request.getPassword(), member.getProviderId())) {
            throw new BusinessLogicException(ExceptionCode.PASSWORD_MISMATCH);
        }

        // 관리자 로그인 성공 - JWT 생성
        String base64EncodedSecretKey = jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(member.getMemberId(), base64EncodedSecretKey);
        String refreshToken = jwtTokenizer.generateRefreshToken(member.getMemberId(), base64EncodedSecretKey);

        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        return new LoginResponse(accessToken, refreshToken, member.getMemberId());
    }
}
