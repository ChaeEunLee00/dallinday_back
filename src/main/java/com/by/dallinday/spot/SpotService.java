package com.by.dallinday.spot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotAPIClient spotAPIClient;

    // 지역 기반 관광지 리스트 조회
    public List<SpotItem> findSpotsByArea (int areaCode, int numOfRows, int pageNo) {
        // spotApiClient를 통해 외부 API 호출
        return spotAPIClient.callAreaBasedAPI(areaCode, numOfRows, pageNo);
    }

    // 위치 기반 관광지 리스트 조회
    public List<SpotItem> findSpotsByLocation(double mapX, double mapY, int areaCode, int radius, int numOfRows, int pageNo) {
        // spotApiClient를 통해 외부 API 호출
        return spotAPIClient.callLocationBasedAPI(mapX, mapY, areaCode, radius, numOfRows, pageNo);
    }
}
