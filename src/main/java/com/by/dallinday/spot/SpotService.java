package com.by.dallinday.spot;

import com.by.dallinday.courseSpot.CourseSpotRepository;
import com.by.dallinday.spot.dto.SpotDetailResponse;
import com.by.dallinday.spot.dto.SpotResponse;
import com.by.dallinday.spot.tourAPI.*;
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

    // 관광지 조회
    public SpotDetailResponse findSpot(Long spotId) {

        // spotApiClient를 통해 외부 API 호출
        SpotCommon spotCommon = spotAPIClient.callCommonInfoAPI(spotId);
        SpotDetail spotDetail = spotAPIClient.callDetailInfoAPI(spotId);
        List<SpotRepeat> spotRepeats = spotAPIClient.callRepeatInfoAPI(spotId)
                .stream()
                .map(sr -> {
                    sr.setInfoname(sr.getInfoname().replaceAll("\\s+", ""));
                    return sr;
                })
                .toList();

        SpotDetailResponse spotDetailResponse = new SpotDetailResponse();
        spotDetailResponse.setSpotId(spotCommon.getContentid());
        spotDetailResponse.setName(spotCommon.getTitle());
        spotDetailResponse.setAddr1(spotCommon.getAddr1());
        spotDetailResponse.setAddr2(spotCommon.getAddr2());
        spotDetailResponse.setDescription(spotCommon.getOverview());
        spotDetailResponse.setClosedDays(spotDetail.getRestdate());
        spotDetailResponse.setOpeningHours(spotDetail.getUsetime());
        spotDetailResponse.setParking(spotDetail.getParking());
        spotDetailResponse.setBabyCarriage(spotDetail.getChkbabycarriage());
        spotDetailResponse.setCreditCard(spotDetail.getChkcreditcard());
        spotDetailResponse.setInfoCenter(spotDetail.getInfocenter());
        spotDetailResponse.setEtc(spotRepeats);

        return spotDetailResponse;
    }
}
