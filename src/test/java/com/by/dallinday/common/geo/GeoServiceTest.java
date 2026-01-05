package com.by.dallinday.common.geo;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("GeoService 단위 테스트")
class GeoServiceTest {

    @Nested
    @DisplayName("coordToAddress 메서드")
    class CoordToAddressTest {

        @Test
        @DisplayName("API 키가 설정되지 않으면 예외가 발생한다")
        void coordToAddress_withoutApiKey_throwsException() {
            // Given
            GeoService geoService = new GeoService();
            ReflectionTestUtils.setField(geoService, "kakaoApiKey", null);

            // When & Then
            assertThatThrownBy(() -> geoService.coordToAddress(127.0, 37.0))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.EXTERNAL_API_ERROR);
        }

        @Test
        @DisplayName("잘못된 API 키로 호출하면 예외가 발생한다")
        void coordToAddress_withInvalidApiKey_throwsException() {
            // Given
            GeoService geoService = new GeoService();
            ReflectionTestUtils.setField(geoService, "kakaoApiKey", "invalidApiKey");

            // When & Then
            assertThatThrownBy(() -> geoService.coordToAddress(127.0, 37.0))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.EXTERNAL_API_ERROR);
        }

        @Test
        @DisplayName("유효하지 않은 좌표로 호출하면 예외가 발생한다")
        void coordToAddress_withInvalidCoordinates_throwsException() {
            // Given
            GeoService geoService = new GeoService();
            ReflectionTestUtils.setField(geoService, "kakaoApiKey", "testApiKey");

            // When & Then
            assertThatThrownBy(() -> geoService.coordToAddress(0.0, 0.0))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.EXTERNAL_API_ERROR);
        }
    }

    @Nested
    @DisplayName("GeoResponse 테스트")
    class GeoResponseTest {

        @Test
        @DisplayName("GeoResponse가 올바르게 생성된다")
        void geoResponse_createsCorrectly() {
            // Given
            String address = "제주특별자치도 제주시 연동";
            String roadAddress = "제주특별자치도 제주시 연삼로 123";

            // When
            GeoResponse response = new GeoResponse(address, roadAddress);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getAddress()).isEqualTo(address);
            assertThat(response.getRoadAddress()).isEqualTo(roadAddress);
        }

        @Test
        @DisplayName("GeoResponse에 null 값을 설정할 수 있다")
        void geoResponse_withNullValues_createsCorrectly() {
            // Given & When
            GeoResponse response = new GeoResponse(null, null);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getAddress()).isNull();
            assertThat(response.getRoadAddress()).isNull();
        }

        @Test
        @DisplayName("주소만 있고 도로명 주소가 없는 경우")
        void geoResponse_withOnlyAddress() {
            // Given
            String address = "제주특별자치도 제주시 연동";

            // When
            GeoResponse response = new GeoResponse(address, null);

            // Then
            assertThat(response.getAddress()).isEqualTo(address);
            assertThat(response.getRoadAddress()).isNull();
        }

        @Test
        @DisplayName("도로명 주소만 있고 지번 주소가 없는 경우")
        void geoResponse_withOnlyRoadAddress() {
            // Given
            String roadAddress = "제주특별자치도 제주시 연삼로 123";

            // When
            GeoResponse response = new GeoResponse(null, roadAddress);

            // Then
            assertThat(response.getAddress()).isNull();
            assertThat(response.getRoadAddress()).isEqualTo(roadAddress);
        }
    }

    @Nested
    @DisplayName("예외 처리 테스트")
    class ExceptionHandlingTest {

        @Test
        @DisplayName("네트워크 오류 발생 시 EXTERNAL_API_ERROR 예외가 발생한다")
        void coordToAddress_networkError_throwsExternalApiError() {
            // Given
            GeoService geoService = new GeoService();
            ReflectionTestUtils.setField(geoService, "kakaoApiKey", "testApiKey");

            // When & Then
            assertThatThrownBy(() -> geoService.coordToAddress(127.0, 37.0))
                    .isInstanceOf(BusinessLogicException.class)
                    .satisfies(ex -> {
                        BusinessLogicException ble = (BusinessLogicException) ex;
                        assertThat(ble.getExceptionCode()).isEqualTo(ExceptionCode.EXTERNAL_API_ERROR);
                        assertThat(ble.getExceptionCode().getStatus()).isEqualTo(502);
                    });
        }
    }
}
