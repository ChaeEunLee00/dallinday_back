package com.by.dallinday.common.geo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geo")
@RequiredArgsConstructor
public class GeoController {

    private final GeoService geoService;

    @GetMapping("/address")
    public ResponseEntity getAddressFromCoord(
            @RequestParam @Min(-180) @Max(180) double lon,
            @RequestParam @Min(-90) @Max(90) double lat) {

        GeoResponse response = geoService.coordToAddress(lon, lat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
