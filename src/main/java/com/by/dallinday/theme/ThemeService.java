package com.by.dallinday.theme;

import com.by.dallinday.member.Member;
import com.by.dallinday.spot.SpotRepository;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    // 테마 리스트 조회
    public Theme findThemeList() {
        return new Theme();
    }
}
