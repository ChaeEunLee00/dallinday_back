package com.by.dallinday.theme;

import com.by.dallinday.member.Member;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {
//    private final ThemeRepository themeRepository;

    // 테마 리스트 조회
    public Theme findThemeList() {
        return new Theme();
    }
}
