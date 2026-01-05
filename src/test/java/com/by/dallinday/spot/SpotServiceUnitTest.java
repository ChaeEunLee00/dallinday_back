package com.by.dallinday.spot;

import com.by.dallinday.courseSpot.CourseSpotRepository;
import com.by.dallinday.spot.dto.SpotDetailResponse;
import com.by.dallinday.spot.dto.SpotResponse;
import com.by.dallinday.spot.tourAPI.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpotService 단위 테스트")
class SpotServiceUnitTest {

    @Mock
    private SpotMapper spotMapper;

    @Mock
    private SpotAPIClient spotAPIClient;

    @Mock
    private CourseSpotRepository courseSpotRepository;

    @InjectMocks
    private SpotService spotService;

    private SpotItem testSpotItem;
    private SpotResponse testSpotResponse;
    private SpotCommon testSpotCommon;
    private SpotDetail testSpotDetail;
    private SpotRepeat testSpotRepeat;

    @BeforeEach
    void setUp() {
        testSpotItem = new SpotItem();
        testSpotItem.setSpotId(1L);
        testSpotItem.setTitle("테스트 관광지");
        testSpotItem.setMapx(127.0);
        testSpotItem.setMapy(37.0);
        testSpotItem.setDist(500.0);
        testSpotItem.setFirstimage("https://example.com/image.jpg");
        testSpotItem.setAddr1("제주시 테스트로 123");
        testSpotItem.setAddr2("테스트동");
        testSpotItem.setZipcode("12345");
        testSpotItem.setAreacode("7");
        testSpotItem.setContenttypeid("12");

        testSpotResponse = new SpotResponse();
        testSpotResponse.setSpotId(1L);
        testSpotResponse.setName("테스트 관광지");
        testSpotResponse.setLongitude(127.0);
        testSpotResponse.setLatitude(37.0);
        testSpotResponse.setDist(500.0);

        testSpotCommon = new SpotCommon();
        testSpotCommon.setContentid(1L);
        testSpotCommon.setTitle("테스트 관광지");
        testSpotCommon.setAddr1("제주시 테스트로 123");
        testSpotCommon.setAddr2("테스트동");
        testSpotCommon.setOverview("테스트 관광지 설명입니다.");

        testSpotDetail = new SpotDetail();
        testSpotDetail.setRestdate("월요일");
        testSpotDetail.setUsetime("09:00~18:00");
        testSpotDetail.setParking("주차 가능");
        testSpotDetail.setChkbabycarriage("가능");
        testSpotDetail.setChkcreditcard("가능");
        testSpotDetail.setInfocenter("064-123-4567");

        testSpotRepeat = new SpotRepeat();
        testSpotRepeat.setInfoname("입장료");
        testSpotRepeat.setInfotext("무료");
    }

    @Nested
    @DisplayName("findSpotsByLocation 메서드")
    class FindSpotsByLocationTest {

        @Test
        @DisplayName("위치 기반으로 코스에 포함된 관광지 목록을 반환한다")
        void findSpotsByLocation_returnsFilteredSpots() {
            // Given
            double mapX = 127.0;
            double mapY = 37.0;
            int areaCode = 7;
            int radius = 1000;
            int numOfRows = 10;
            int pageNo = 1;

            Set<Long> existingSpotIds = new HashSet<>();
            existingSpotIds.add(1L);

            List<SpotItem> spotItems = List.of(testSpotItem);

            given(courseSpotRepository.findAllDistinctSpotIds()).willReturn(new ArrayList<>(existingSpotIds));
            given(spotAPIClient.callLocationBasedAPI(mapX, mapY, areaCode, radius, numOfRows, pageNo))
                    .willReturn(spotItems);
            given(spotMapper.spotItemToSpotResponse(testSpotItem)).willReturn(testSpotResponse);

            // When
            List<SpotResponse> result = spotService.findSpotsByLocation(mapX, mapY, areaCode, radius, numOfRows, pageNo);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getSpotId()).isEqualTo(1L);
            verify(spotAPIClient, times(1)).callLocationBasedAPI(mapX, mapY, areaCode, radius, numOfRows, pageNo);
        }

        @Test
        @DisplayName("코스에 포함되지 않은 관광지는 필터링된다")
        void findSpotsByLocation_filtersNonExistingSpots() {
            // Given
            double mapX = 127.0;
            double mapY = 37.0;
            int areaCode = 7;
            int radius = 1000;
            int numOfRows = 10;
            int pageNo = 1;

            SpotItem nonExistingSpot = new SpotItem();
            nonExistingSpot.setSpotId(999L);
            nonExistingSpot.setTitle("코스에 없는 관광지");

            Set<Long> existingSpotIds = new HashSet<>();
            existingSpotIds.add(1L);

            List<SpotItem> spotItems = List.of(testSpotItem, nonExistingSpot);

            given(courseSpotRepository.findAllDistinctSpotIds()).willReturn(new ArrayList<>(existingSpotIds));
            given(spotAPIClient.callLocationBasedAPI(mapX, mapY, areaCode, radius, numOfRows, pageNo))
                    .willReturn(spotItems);
            given(spotMapper.spotItemToSpotResponse(testSpotItem)).willReturn(testSpotResponse);

            // When
            List<SpotResponse> result = spotService.findSpotsByLocation(mapX, mapY, areaCode, radius, numOfRows, pageNo);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getSpotId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("API 결과가 비어있으면 빈 목록을 반환한다")
        void findSpotsByLocation_withEmptyApiResult_returnsEmptyList() {
            // Given
            double mapX = 127.0;
            double mapY = 37.0;
            int areaCode = 7;
            int radius = 1000;
            int numOfRows = 10;
            int pageNo = 1;

            given(courseSpotRepository.findAllDistinctSpotIds()).willReturn(new ArrayList<>());
            given(spotAPIClient.callLocationBasedAPI(mapX, mapY, areaCode, radius, numOfRows, pageNo))
                    .willReturn(new ArrayList<>());

            // When
            List<SpotResponse> result = spotService.findSpotsByLocation(mapX, mapY, areaCode, radius, numOfRows, pageNo);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("코스에 등록된 관광지가 없으면 빈 목록을 반환한다")
        void findSpotsByLocation_withNoExistingSpots_returnsEmptyList() {
            // Given
            double mapX = 127.0;
            double mapY = 37.0;
            int areaCode = 7;
            int radius = 1000;
            int numOfRows = 10;
            int pageNo = 1;

            List<SpotItem> spotItems = List.of(testSpotItem);

            given(courseSpotRepository.findAllDistinctSpotIds()).willReturn(new ArrayList<>());
            given(spotAPIClient.callLocationBasedAPI(mapX, mapY, areaCode, radius, numOfRows, pageNo))
                    .willReturn(spotItems);

            // When
            List<SpotResponse> result = spotService.findSpotsByLocation(mapX, mapY, areaCode, radius, numOfRows, pageNo);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("여러 관광지가 코스에 포함되어 있으면 모두 반환한다")
        void findSpotsByLocation_withMultipleExistingSpots_returnsAll() {
            // Given
            double mapX = 127.0;
            double mapY = 37.0;
            int areaCode = 7;
            int radius = 1000;
            int numOfRows = 10;
            int pageNo = 1;

            SpotItem spotItem2 = new SpotItem();
            spotItem2.setSpotId(2L);
            spotItem2.setTitle("두 번째 관광지");

            SpotResponse spotResponse2 = new SpotResponse();
            spotResponse2.setSpotId(2L);
            spotResponse2.setName("두 번째 관광지");

            Set<Long> existingSpotIds = new HashSet<>();
            existingSpotIds.add(1L);
            existingSpotIds.add(2L);

            List<SpotItem> spotItems = List.of(testSpotItem, spotItem2);

            given(courseSpotRepository.findAllDistinctSpotIds()).willReturn(new ArrayList<>(existingSpotIds));
            given(spotAPIClient.callLocationBasedAPI(mapX, mapY, areaCode, radius, numOfRows, pageNo))
                    .willReturn(spotItems);
            given(spotMapper.spotItemToSpotResponse(testSpotItem)).willReturn(testSpotResponse);
            given(spotMapper.spotItemToSpotResponse(spotItem2)).willReturn(spotResponse2);

            // When
            List<SpotResponse> result = spotService.findSpotsByLocation(mapX, mapY, areaCode, radius, numOfRows, pageNo);

            // Then
            assertThat(result).hasSize(2);
        }
    }

    @Nested
    @DisplayName("findSpot 메서드")
    class FindSpotTest {

        @Test
        @DisplayName("관광지 상세 정보를 성공적으로 조회한다")
        void findSpot_withValidId_returnsSpotDetailResponse() {
            // Given
            Long spotId = 1L;

            given(spotAPIClient.callCommonInfoAPI(spotId)).willReturn(testSpotCommon);
            given(spotAPIClient.callDetailInfoAPI(spotId)).willReturn(testSpotDetail);
            given(spotAPIClient.callRepeatInfoAPI(spotId)).willReturn(List.of(testSpotRepeat));

            // When
            SpotDetailResponse result = spotService.findSpot(spotId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getSpotId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("테스트 관광지");
            assertThat(result.getDescription()).isEqualTo("테스트 관광지 설명입니다.");
            assertThat(result.getClosedDays()).isEqualTo("월요일");
            assertThat(result.getOpeningHours()).isEqualTo("09:00~18:00");
            assertThat(result.getParking()).isEqualTo("주차 가능");
            assertThat(result.getBabyCarriage()).isEqualTo("가능");
            assertThat(result.getCreditCard()).isEqualTo("가능");
            assertThat(result.getInfoCenter()).isEqualTo("064-123-4567");
        }

        @Test
        @DisplayName("관광지 정보에서 주소가 올바르게 설정된다")
        void findSpot_setsAddressCorrectly() {
            // Given
            Long spotId = 1L;

            given(spotAPIClient.callCommonInfoAPI(spotId)).willReturn(testSpotCommon);
            given(spotAPIClient.callDetailInfoAPI(spotId)).willReturn(testSpotDetail);
            given(spotAPIClient.callRepeatInfoAPI(spotId)).willReturn(new ArrayList<>());

            // When
            SpotDetailResponse result = spotService.findSpot(spotId);

            // Then
            assertThat(result.getAddr1()).isEqualTo("제주시 테스트로 123");
            assertThat(result.getAddr2()).isEqualTo("테스트동");
        }

        @Test
        @DisplayName("반복 정보의 공백이 제거된다")
        void findSpot_removesWhitespaceFromRepeatInfo() {
            // Given
            Long spotId = 1L;

            SpotRepeat repeatWithWhitespace = new SpotRepeat();
            repeatWithWhitespace.setInfoname("입 장 료");
            repeatWithWhitespace.setInfotext("무료");

            given(spotAPIClient.callCommonInfoAPI(spotId)).willReturn(testSpotCommon);
            given(spotAPIClient.callDetailInfoAPI(spotId)).willReturn(testSpotDetail);
            given(spotAPIClient.callRepeatInfoAPI(spotId)).willReturn(List.of(repeatWithWhitespace));

            // When
            SpotDetailResponse result = spotService.findSpot(spotId);

            // Then
            assertThat(result.getEtc()).isNotNull();
            assertThat(result.getEtc()).hasSize(1);
            assertThat(result.getEtc().get(0).getInfoname()).isEqualTo("입장료");
        }

        @Test
        @DisplayName("반복 정보가 없으면 빈 리스트를 반환한다")
        void findSpot_withNoRepeatInfo_returnsEmptyList() {
            // Given
            Long spotId = 1L;

            given(spotAPIClient.callCommonInfoAPI(spotId)).willReturn(testSpotCommon);
            given(spotAPIClient.callDetailInfoAPI(spotId)).willReturn(testSpotDetail);
            given(spotAPIClient.callRepeatInfoAPI(spotId)).willReturn(new ArrayList<>());

            // When
            SpotDetailResponse result = spotService.findSpot(spotId);

            // Then
            assertThat(result.getEtc()).isEmpty();
        }

        @Test
        @DisplayName("여러 반복 정보를 올바르게 처리한다")
        void findSpot_withMultipleRepeatInfo_processesAll() {
            // Given
            Long spotId = 1L;

            SpotRepeat repeat1 = new SpotRepeat();
            repeat1.setInfoname("입 장 료");
            repeat1.setInfotext("무료");

            SpotRepeat repeat2 = new SpotRepeat();
            repeat2.setInfoname("운영시간");
            repeat2.setInfotext("09:00~18:00");

            SpotRepeat repeat3 = new SpotRepeat();
            repeat3.setInfoname("주 차");
            repeat3.setInfotext("가능");

            given(spotAPIClient.callCommonInfoAPI(spotId)).willReturn(testSpotCommon);
            given(spotAPIClient.callDetailInfoAPI(spotId)).willReturn(testSpotDetail);
            given(spotAPIClient.callRepeatInfoAPI(spotId)).willReturn(List.of(repeat1, repeat2, repeat3));

            // When
            SpotDetailResponse result = spotService.findSpot(spotId);

            // Then
            assertThat(result.getEtc()).hasSize(3);
            assertThat(result.getEtc().get(0).getInfoname()).isEqualTo("입장료");
            assertThat(result.getEtc().get(1).getInfoname()).isEqualTo("운영시간");
            assertThat(result.getEtc().get(2).getInfoname()).isEqualTo("주차");
        }

        @Test
        @DisplayName("외부 API가 3번 호출된다")
        void findSpot_callsAllApis() {
            // Given
            Long spotId = 1L;

            given(spotAPIClient.callCommonInfoAPI(spotId)).willReturn(testSpotCommon);
            given(spotAPIClient.callDetailInfoAPI(spotId)).willReturn(testSpotDetail);
            given(spotAPIClient.callRepeatInfoAPI(spotId)).willReturn(new ArrayList<>());

            // When
            spotService.findSpot(spotId);

            // Then
            verify(spotAPIClient, times(1)).callCommonInfoAPI(spotId);
            verify(spotAPIClient, times(1)).callDetailInfoAPI(spotId);
            verify(spotAPIClient, times(1)).callRepeatInfoAPI(spotId);
        }
    }

    @Nested
    @DisplayName("SpotMapper 통합 테스트")
    class SpotMapperIntegrationTest {

        @Test
        @DisplayName("SpotMapper가 SpotItem을 SpotResponse로 올바르게 변환한다")
        void spotMapper_convertsCorrectly() {
            // Given
            double mapX = 127.0;
            double mapY = 37.0;
            int areaCode = 7;
            int radius = 1000;
            int numOfRows = 10;
            int pageNo = 1;

            Set<Long> existingSpotIds = new HashSet<>();
            existingSpotIds.add(1L);

            SpotResponse mappedResponse = new SpotResponse();
            mappedResponse.setSpotId(1L);
            mappedResponse.setName("테스트 관광지");
            mappedResponse.setLongitude(127.0);
            mappedResponse.setLatitude(37.0);
            mappedResponse.setDist(500.0);
            mappedResponse.setFirstimage("https://example.com/image.jpg");
            mappedResponse.setAddr1("제주시 테스트로 123");
            mappedResponse.setAddr2("테스트동");
            mappedResponse.setZipcode("12345");
            mappedResponse.setAreacode("7");
            mappedResponse.setContenttypeid("12");

            given(courseSpotRepository.findAllDistinctSpotIds()).willReturn(new ArrayList<>(existingSpotIds));
            given(spotAPIClient.callLocationBasedAPI(mapX, mapY, areaCode, radius, numOfRows, pageNo))
                    .willReturn(List.of(testSpotItem));
            given(spotMapper.spotItemToSpotResponse(testSpotItem)).willReturn(mappedResponse);

            // When
            List<SpotResponse> result = spotService.findSpotsByLocation(mapX, mapY, areaCode, radius, numOfRows, pageNo);

            // Then
            assertThat(result).hasSize(1);
            SpotResponse response = result.get(0);
            assertThat(response.getSpotId()).isEqualTo(1L);
            assertThat(response.getName()).isEqualTo("테스트 관광지");
            assertThat(response.getLongitude()).isEqualTo(127.0);
            assertThat(response.getLatitude()).isEqualTo(37.0);
            assertThat(response.getDist()).isEqualTo(500.0);
            assertThat(response.getFirstimage()).isEqualTo("https://example.com/image.jpg");
        }
    }
}
