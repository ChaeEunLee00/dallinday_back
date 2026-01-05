package com.by.dallinday.theme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("ThemeService 단위 테스트")
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeService = new ThemeService(themeRepository);
    }

    @Nested
    @DisplayName("findThemeList 메서드")
    class FindThemeListTest {

        @Test
        @DisplayName("테마 리스트 조회 시 새로운 Theme 객체를 반환한다")
        void findThemeList_returnsNewTheme() {
            // Given
            // 현재 구현은 단순히 new Theme()을 반환

            // When
            Theme result = themeService.findThemeList();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(Theme.class);
        }

        @Test
        @DisplayName("반환된 Theme 객체의 필드는 초기값이다")
        void findThemeList_returnsThemeWithDefaultValues() {
            // Given
            // 현재 구현은 단순히 new Theme()을 반환

            // When
            Theme result = themeService.findThemeList();

            // Then
            assertThat(result.getThemeId()).isNull();
            assertThat(result.getTitle()).isNull();
            assertThat(result.getDescription()).isNull();
            assertThat(result.getImageUrl()).isNull();
            assertThat(result.getCreatedAt()).isNull();
        }

        @Test
        @DisplayName("여러 번 호출해도 매번 새로운 객체를 반환한다")
        void findThemeList_returnsNewInstanceEachTime() {
            // Given
            // 현재 구현은 단순히 new Theme()을 반환

            // When
            Theme result1 = themeService.findThemeList();
            Theme result2 = themeService.findThemeList();

            // Then
            assertThat(result1).isNotSameAs(result2);
        }
    }

    @Nested
    @DisplayName("ThemeService 생성자 테스트")
    class ConstructorTest {

        @Test
        @DisplayName("ThemeRepository가 주입되어 정상적으로 생성된다")
        void constructor_withRepository_createsService() {
            // Given
            ThemeRepository repository = themeRepository;

            // When
            ThemeService service = new ThemeService(repository);

            // Then
            assertThat(service).isNotNull();
        }
    }
}
