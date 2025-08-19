package com.by.dallinday.spot;

import com.by.dallinday.coursespot.CourseSpotRepository;
import com.by.dallinday.spot.dto.SpotResponse;
import com.by.dallinday.spot.tourAPI.SpotAPIClient;
import com.by.dallinday.spot.tourAPI.SpotItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotMapper spotMapper;
    private final SpotAPIClient spotAPIClient;
    private final CourseSpotRepository courseSpotRepository;

//    // 지역 기반 관광지 리스트 조회
//    public List<SpotItem> findSpotsByArea (int areaCode, int numOfRows, int pageNo) {
//        // spotApiClient를 통해 외부 API 호출
//        return spotAPIClient.callAreaBasedAPI(areaCode, numOfRows, pageNo);
//    }

    // 위치 기반 관광지 리스트 조회
    public List<SpotResponse> findSpotsByLocation(double mapX, double mapY, int areaCode, int radius, int numOfRows, int pageNo) {

        // 코스에 포함되어있는 모든 spotId 조회
        Set<Long> existing = new HashSet<>(courseSpotRepository.findAllDistinctSpotIds());

        // spotApiClient를 통해 외부 API 호출
        List<SpotItem> spots = spotAPIClient.callLocationBasedAPI(mapX, mapY, areaCode, radius, numOfRows, pageNo);

        return spots.stream()
                .filter(spot -> existing.contains(spot.getSpotId()))
                .map(spot -> spotMapper.spotItemToSpotResponse(spot))
                .toList();
    }
}
