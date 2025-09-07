package com.by.dallinday.spot;

import com.by.dallinday.spot.dto.SpotResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam double lon, @RequestParam double lat,
            @RequestParam(defaultValue = "7") int areaCode,
            @RequestParam(defaultValue = "20000") int radius,
            @RequestParam(defaultValue = "10") @Positive int numOfRows,
            @RequestParam(defaultValue = "1") @Positive int pageNo) {

        List<SpotResponse> responses = spotService.findSpotsByLocation(lon, lat, areaCode, radius, numOfRows, pageNo);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{spot-id}")
    public ResponseEntity getSpot(@PathVariable("spot-id") @Positive Long spotId) {

        return new ResponseEntity<>(spotService.findSpot(spotId), HttpStatus.OK);
    }
}
