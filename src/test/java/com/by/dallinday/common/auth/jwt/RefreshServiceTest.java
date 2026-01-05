package com.by.dallinday.common.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshService 단위 테스트")
class RefreshServiceTest {

    @Mock
    private JwtTokenizer jwtTokenizer;

    @Mock
    private Jws<Claims> jwsClaims;

    @Mock
    private Claims claims;

    @InjectMocks
    private RefreshService refreshService;

    private String testRefreshToken;
    private String testSecretKey;
    private String testBase64EncodedSecretKey;
    private Long testMemberId;

    @BeforeEach
    void setUp() {
        testRefreshToken = "testRefreshToken";
        testSecretKey = "testSecretKey";
        testBase64EncodedSecretKey = "encodedTestSecretKey";
        testMemberId = 1L;
    }

    @Nested
    @DisplayName("createNewAccessToken 메서드")
    class CreateNewAccessTokenTest {

        @Test
        @DisplayName("유효한 리프레시 토큰으로 새 액세스 토큰을 생성한다")
        void createNewAccessToken_withValidRefreshToken_returnsNewAccessToken() {
            // Given
            String expectedAccessToken = "newAccessToken";
            Map<String, Object> claimsMap = new HashMap<>();
            claimsMap.put("memberId", testMemberId);

            given(jwtTokenizer.getSecretKey()).willReturn(testSecretKey);
            given(jwtTokenizer.encodedBase64SecretKey(testSecretKey)).willReturn(testBase64EncodedSecretKey);
            given(jwtTokenizer.getClaims(testRefreshToken, testBase64EncodedSecretKey)).willReturn(jwsClaims);
            given(jwsClaims.getBody()).willReturn(claims);
            given(claims.get("memberId")).willReturn(testMemberId);
            given(jwtTokenizer.generateAccessToken(testMemberId, testBase64EncodedSecretKey))
                    .willReturn(expectedAccessToken);

            // When
            String result = refreshService.createNewAccessToken(testRefreshToken);

            // Then
            assertThat(result).isEqualTo(expectedAccessToken);
        }

        @Test
        @DisplayName("리프레시 토큰에서 memberId를 추출한다")
        void createNewAccessToken_extractsMemberIdFromToken() {
            // Given
            String expectedAccessToken = "newAccessToken";

            given(jwtTokenizer.getSecretKey()).willReturn(testSecretKey);
            given(jwtTokenizer.encodedBase64SecretKey(testSecretKey)).willReturn(testBase64EncodedSecretKey);
            given(jwtTokenizer.getClaims(testRefreshToken, testBase64EncodedSecretKey)).willReturn(jwsClaims);
            given(jwsClaims.getBody()).willReturn(claims);
            given(claims.get("memberId")).willReturn(testMemberId);
            given(jwtTokenizer.generateAccessToken(testMemberId, testBase64EncodedSecretKey))
                    .willReturn(expectedAccessToken);

            // When
            refreshService.createNewAccessToken(testRefreshToken);

            // Then
            verify(jwtTokenizer, times(1)).getClaims(testRefreshToken, testBase64EncodedSecretKey);
            verify(claims, times(1)).get("memberId");
        }

        @Test
        @DisplayName("추출된 memberId로 새 액세스 토큰을 생성한다")
        void createNewAccessToken_generatesTokenWithExtractedMemberId() {
            // Given
            Long specificMemberId = 999L;
            String expectedAccessToken = "newAccessToken";

            given(jwtTokenizer.getSecretKey()).willReturn(testSecretKey);
            given(jwtTokenizer.encodedBase64SecretKey(testSecretKey)).willReturn(testBase64EncodedSecretKey);
            given(jwtTokenizer.getClaims(testRefreshToken, testBase64EncodedSecretKey)).willReturn(jwsClaims);
            given(jwsClaims.getBody()).willReturn(claims);
            given(claims.get("memberId")).willReturn(specificMemberId);
            given(jwtTokenizer.generateAccessToken(specificMemberId, testBase64EncodedSecretKey))
                    .willReturn(expectedAccessToken);

            // When
            refreshService.createNewAccessToken(testRefreshToken);

            // Then
            verify(jwtTokenizer, times(1)).generateAccessToken(specificMemberId, testBase64EncodedSecretKey);
        }

        @Test
        @DisplayName("비밀키가 Base64로 인코딩된다")
        void createNewAccessToken_encodesSecretKeyToBase64() {
            // Given
            given(jwtTokenizer.getSecretKey()).willReturn(testSecretKey);
            given(jwtTokenizer.encodedBase64SecretKey(testSecretKey)).willReturn(testBase64EncodedSecretKey);
            given(jwtTokenizer.getClaims(testRefreshToken, testBase64EncodedSecretKey)).willReturn(jwsClaims);
            given(jwsClaims.getBody()).willReturn(claims);
            given(claims.get("memberId")).willReturn(testMemberId);
            given(jwtTokenizer.generateAccessToken(any(Long.class), anyString())).willReturn("token");

            // When
            refreshService.createNewAccessToken(testRefreshToken);

            // Then
            verify(jwtTokenizer, times(1)).getSecretKey();
            verify(jwtTokenizer, times(1)).encodedBase64SecretKey(testSecretKey);
        }

        @Test
        @DisplayName("memberId가 String으로 저장되어도 Long으로 변환된다")
        void createNewAccessToken_convertsStringMemberIdToLong() {
            // Given
            String memberIdAsString = "123";
            String expectedAccessToken = "newAccessToken";

            given(jwtTokenizer.getSecretKey()).willReturn(testSecretKey);
            given(jwtTokenizer.encodedBase64SecretKey(testSecretKey)).willReturn(testBase64EncodedSecretKey);
            given(jwtTokenizer.getClaims(testRefreshToken, testBase64EncodedSecretKey)).willReturn(jwsClaims);
            given(jwsClaims.getBody()).willReturn(claims);
            given(claims.get("memberId")).willReturn(memberIdAsString);
            given(jwtTokenizer.generateAccessToken(123L, testBase64EncodedSecretKey))
                    .willReturn(expectedAccessToken);

            // When
            String result = refreshService.createNewAccessToken(testRefreshToken);

            // Then
            assertThat(result).isEqualTo(expectedAccessToken);
            verify(jwtTokenizer, times(1)).generateAccessToken(123L, testBase64EncodedSecretKey);
        }

        @Test
        @DisplayName("Integer memberId도 Long으로 변환된다")
        void createNewAccessToken_convertsIntegerMemberIdToLong() {
            // Given
            Integer memberIdAsInteger = 456;
            String expectedAccessToken = "newAccessToken";

            given(jwtTokenizer.getSecretKey()).willReturn(testSecretKey);
            given(jwtTokenizer.encodedBase64SecretKey(testSecretKey)).willReturn(testBase64EncodedSecretKey);
            given(jwtTokenizer.getClaims(testRefreshToken, testBase64EncodedSecretKey)).willReturn(jwsClaims);
            given(jwsClaims.getBody()).willReturn(claims);
            given(claims.get("memberId")).willReturn(memberIdAsInteger);
            given(jwtTokenizer.generateAccessToken(456L, testBase64EncodedSecretKey))
                    .willReturn(expectedAccessToken);

            // When
            String result = refreshService.createNewAccessToken(testRefreshToken);

            // Then
            assertThat(result).isEqualTo(expectedAccessToken);
            verify(jwtTokenizer, times(1)).generateAccessToken(456L, testBase64EncodedSecretKey);
        }
    }

    @Nested
    @DisplayName("토큰 검증 테스트")
    class TokenVerificationTest {

        @Test
        @DisplayName("getClaims 호출 시 토큰이 검증된다")
        void createNewAccessToken_verifiesTokenThroughGetClaims() {
            // Given
            given(jwtTokenizer.getSecretKey()).willReturn(testSecretKey);
            given(jwtTokenizer.encodedBase64SecretKey(testSecretKey)).willReturn(testBase64EncodedSecretKey);
            given(jwtTokenizer.getClaims(testRefreshToken, testBase64EncodedSecretKey)).willReturn(jwsClaims);
            given(jwsClaims.getBody()).willReturn(claims);
            given(claims.get("memberId")).willReturn(testMemberId);
            given(jwtTokenizer.generateAccessToken(any(Long.class), anyString())).willReturn("token");

            // When
            refreshService.createNewAccessToken(testRefreshToken);

            // Then
            verify(jwtTokenizer, times(1)).getClaims(testRefreshToken, testBase64EncodedSecretKey);
        }
    }
}
