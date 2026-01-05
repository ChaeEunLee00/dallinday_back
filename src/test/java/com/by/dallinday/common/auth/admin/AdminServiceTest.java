package com.by.dallinday.common.auth.admin;

import com.by.dallinday.common.auth.jwt.JwtTokenizer;
import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdminService 단위 테스트")
class AdminServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenizer jwtTokenizer;

    @InjectMocks
    private AdminService adminService;

    private Member testAdmin;
    private LoginRequest testLoginRequest;

    @BeforeEach
    void setUp() {
        testAdmin = new Member();
        testAdmin.setMemberId(1L);
        testAdmin.setEmail("admin@dallinday.com");
        testAdmin.setUsername("관리자");
        testAdmin.setProvider("local");
        testAdmin.setProviderId("encodedPassword123");
        testAdmin.setRole("ADMIN");

        testLoginRequest = new LoginRequest();
        ReflectionTestUtils.setField(testLoginRequest, "id", "admin@dallinday.com");
        ReflectionTestUtils.setField(testLoginRequest, "password", "password123");
    }

    @Nested
    @DisplayName("login 메서드")
    class LoginTest {

        @Test
        @DisplayName("올바른 자격 증명으로 로그인하면 토큰을 반환한다")
        void login_withValidCredentials_returnsTokens() {
            // Given
            String base64EncodedSecretKey = "encodedSecretKey";
            String accessToken = "testAccessToken";
            String refreshToken = "testRefreshToken";

            given(memberRepository.findByEmail(testLoginRequest.getId())).willReturn(Optional.of(testAdmin));
            given(passwordEncoder.matches(testLoginRequest.getPassword(), testAdmin.getProviderId())).willReturn(true);
            given(jwtTokenizer.getSecretKey()).willReturn("secretKey");
            given(jwtTokenizer.encodedBase64SecretKey(anyString())).willReturn(base64EncodedSecretKey);
            given(jwtTokenizer.generateAccessToken(any(Long.class), anyString())).willReturn(accessToken);
            given(jwtTokenizer.generateRefreshToken(any(Long.class), anyString())).willReturn(refreshToken);

            // When
            LoginResponse result = adminService.login(testLoginRequest);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getAccessToken()).isEqualTo(accessToken);
            assertThat(result.getRefreshToken()).isEqualTo(refreshToken);
            assertThat(result.getMemberId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("존재하지 않는 이메일로 로그인하면 예외가 발생한다")
        void login_withInvalidEmail_throwsException() {
            // Given
            LoginRequest invalidRequest = new LoginRequest();
            ReflectionTestUtils.setField(invalidRequest, "id", "invalid@email.com");
            ReflectionTestUtils.setField(invalidRequest, "password", "password123");

            given(memberRepository.findByEmail(invalidRequest.getId())).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> adminService.login(invalidRequest))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.MEMBER_NOT_FOUND);
        }

        @Test
        @DisplayName("잘못된 비밀번호로 로그인하면 예외가 발생한다")
        void login_withWrongPassword_throwsException() {
            // Given
            LoginRequest wrongPasswordRequest = new LoginRequest();
            ReflectionTestUtils.setField(wrongPasswordRequest, "id", "admin@dallinday.com");
            ReflectionTestUtils.setField(wrongPasswordRequest, "password", "wrongPassword");

            given(memberRepository.findByEmail(wrongPasswordRequest.getId())).willReturn(Optional.of(testAdmin));
            given(passwordEncoder.matches(wrongPasswordRequest.getPassword(), testAdmin.getProviderId())).willReturn(false);

            // When & Then
            assertThatThrownBy(() -> adminService.login(wrongPasswordRequest))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.PASSWORD_MISMATCH);
        }

        @Test
        @DisplayName("로그인 성공 시 JWT 토큰이 생성된다")
        void login_success_generatesJwtTokens() {
            // Given
            String base64EncodedSecretKey = "encodedSecretKey";
            String accessToken = "generatedAccessToken";
            String refreshToken = "generatedRefreshToken";

            given(memberRepository.findByEmail(testLoginRequest.getId())).willReturn(Optional.of(testAdmin));
            given(passwordEncoder.matches(testLoginRequest.getPassword(), testAdmin.getProviderId())).willReturn(true);
            given(jwtTokenizer.getSecretKey()).willReturn("secretKey");
            given(jwtTokenizer.encodedBase64SecretKey(anyString())).willReturn(base64EncodedSecretKey);
            given(jwtTokenizer.generateAccessToken(any(Long.class), anyString())).willReturn(accessToken);
            given(jwtTokenizer.generateRefreshToken(any(Long.class), anyString())).willReturn(refreshToken);

            // When
            adminService.login(testLoginRequest);

            // Then
            verify(jwtTokenizer, times(1)).generateAccessToken(testAdmin.getMemberId(), base64EncodedSecretKey);
            verify(jwtTokenizer, times(1)).generateRefreshToken(testAdmin.getMemberId(), base64EncodedSecretKey);
        }

        @Test
        @DisplayName("로그인 성공 시 올바른 memberId가 반환된다")
        void login_success_returnsCorrectMemberId() {
            // Given
            String base64EncodedSecretKey = "encodedSecretKey";

            given(memberRepository.findByEmail(testLoginRequest.getId())).willReturn(Optional.of(testAdmin));
            given(passwordEncoder.matches(testLoginRequest.getPassword(), testAdmin.getProviderId())).willReturn(true);
            given(jwtTokenizer.getSecretKey()).willReturn("secretKey");
            given(jwtTokenizer.encodedBase64SecretKey(anyString())).willReturn(base64EncodedSecretKey);
            given(jwtTokenizer.generateAccessToken(any(Long.class), anyString())).willReturn("token");
            given(jwtTokenizer.generateRefreshToken(any(Long.class), anyString())).willReturn("token");

            // When
            LoginResponse result = adminService.login(testLoginRequest);

            // Then
            assertThat(result.getMemberId()).isEqualTo(testAdmin.getMemberId());
        }
    }

    @Nested
    @DisplayName("비밀번호 검증 테스트")
    class PasswordVerificationTest {

        @Test
        @DisplayName("PasswordEncoder가 비밀번호 매칭에 사용된다")
        void login_usesPasswordEncoderForMatching() {
            // Given
            given(memberRepository.findByEmail(testLoginRequest.getId())).willReturn(Optional.of(testAdmin));
            given(passwordEncoder.matches(testLoginRequest.getPassword(), testAdmin.getProviderId())).willReturn(true);
            given(jwtTokenizer.getSecretKey()).willReturn("secretKey");
            given(jwtTokenizer.encodedBase64SecretKey(anyString())).willReturn("encoded");
            given(jwtTokenizer.generateAccessToken(any(Long.class), anyString())).willReturn("token");
            given(jwtTokenizer.generateRefreshToken(any(Long.class), anyString())).willReturn("token");

            // When
            adminService.login(testLoginRequest);

            // Then
            verify(passwordEncoder, times(1)).matches(testLoginRequest.getPassword(), testAdmin.getProviderId());
        }
    }
}
