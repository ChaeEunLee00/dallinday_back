package com.by.dallinday.spot;

import com.by.dallinday.spot.dto.SpotResponse;
import com.by.dallinday.spot.tourAPI.SpotItem;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spots")
public class SpotController {
    private final SpotService spotService;

//    // 지역 기반 관광지 리스트 조회
//    @GetMapping("/area")
//    public ResponseEntity<List<SpotItem>> getSpotsByArea (
//            @RequestParam int areaCode,
//            @RequestParam(defaultValue = "10") @Positive int numOfRows,
//            @RequestParam(defaultValue = "1") @Positive int pageNo) {
//
//        // DTO 필요시 사용 - 지금은 x
//        List<SpotItem> spotItemList = spotService.findSpotsByArea(areaCode, numOfRows, pageNo);
//        return new ResponseEntity<>(spotItemList, HttpStatus.OK);
//    }

    // 위치 기반 관광지 리스트 조회
    @GetMapping("/location")
    public ResponseEntity getSpotsByLocation(
            @RequestParam double mapX, @RequestParam double mapY, @RequestParam int areaCode,
            @RequestParam(defaultValue = "20000") int radius,
            @RequestParam(defaultValue = "10") @Positive int numOfRows,
            @RequestParam(defaultValue = "1") @Positive int pageNo) {

        List<SpotResponse> responses = spotService.findSpotsByLocation(mapX, mapY, areaCode, radius, numOfRows, pageNo);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
