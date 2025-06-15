package com.by.dallinday.theme;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/themes")
public class ThemeController {
//    private final ThemeService themeService;

    // 테마 리스트 조회
    @GetMapping
    public ResponseEntity getThemes() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
