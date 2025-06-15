package com.by.dallinday.spot;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spots")
public class SpotController {
//    private final SpotService spotService;

    // 관광지 리스트 조회
    @GetMapping
    public ResponseEntity getSpots() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
