package com.by.dallinday.course;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.common.gpx.GpxResult;
import com.by.dallinday.common.gpx.GpxUtil;
import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.course.dto.CourseResponse;
import com.by.dallinday.course.dto.CourseSpotResponse;
import com.by.dallinday.course.dto.CourseTop5Response;
import com.by.dallinday.course.tourAPI.CourseAPIClient;
import com.by.dallinday.course.tourAPI.CourseItem;
import com.by.dallinday.courseSpot.CourseSpot;
import com.by.dallinday.courseSpot.CourseSpotRepository;
import com.by.dallinday.favorite.Favorite;
import com.by.dallinday.favorite.FavoriteRepository;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import com.by.dallinday.spot.tourAPI.SpotAPIClient;
import com.by.dallinday.spot.tourAPI.SpotItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService 단위 테스트")
class CourseServiceTest {

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private SpotAPIClient spotAPIClient;

    @Mock
    private CourseAPIClient courseAPIClient;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private CourseSpotRepository courseSpotRepository;

    @Mock
    private GpxUtil gpxUtil;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;
    private Member testMember;
    private CourseResponse testCourseResponse;
    private CourseListResponse testCourseListResponse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId(1L);
        testCourse.setName("테스트 코스");
        testCourse.setDescription("테스트 설명");
        testCourse.setDistance(5.0);
        testCourse.setDuration(60);
        testCourse.setDifficulty(2);
        testCourse.setGpxpath("/test/path.gpx");
        testCourse.setCourseSpotList(new ArrayList<>());
        testCourse.setRunList(new ArrayList<>());
        testCourse.setCreatedAt(LocalDateTime.now());
        testCourse.setModifiedAt(LocalDateTime.now());

        testMember = new Member();
        testMember.setMemberId(1L);
        testMember.setEmail("test@test.com");
        testMember.setUsername("테스트유저");
        testMember.setProvider("kakao");
        testMember.setProviderId("12345");
        testMember.setRole("USER");

        testCourseResponse = new CourseResponse();
        testCourseResponse.setCourseId(1L);
        testCourseResponse.setName("테스트 코스");
        testCourseResponse.setCourseSpotList(new ArrayList<>());

        testCourseListResponse = new CourseListResponse();
        testCourseListResponse.setCourseId(1L);
        testCourseListResponse.setName("테스트 코스");
        testCourseListResponse.setCourseSpotList(new ArrayList<>());
    }

    @Nested
    @DisplayName("findCourse 메서드")
    class FindCourseTest {

        @Test
        @DisplayName("존재하는 코스 ID로 조회하면 코스 정보를 반환한다")
        void findCourse_withValidId_returnsCourseResponse() {
            // Given
            Long courseId = 1L;
            given(courseRepository.findById(courseId)).willReturn(Optional.of(testCourse));
            given(courseMapper.courseToCourseResponse(testCourse)).willReturn(testCourseResponse);

            // When
            CourseResponse result = courseService.findCourse(courseId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getCourseId()).isEqualTo(courseId);
            verify(courseRepository, times(1)).findById(courseId);
            verify(courseMapper, times(1)).courseToCourseResponse(testCourse);
        }

        @Test
        @DisplayName("존재하지 않는 코스 ID로 조회하면 예외가 발생한다")
        void findCourse_withInvalidId_throwsException() {
            // Given
            Long invalidCourseId = 999L;
            given(courseRepository.findById(invalidCourseId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courseService.findCourse(invalidCourseId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.COURSE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("findSpotCourseList 메서드")
    class FindSpotCourseListTest {

        @Test
        @DisplayName("거리순으로 정렬된 코스 리스트를 반환한다")
        void findSpotCourseList_sortByDistance_returnsOrderedList() {
            // Given
            Long spotId = 1L;
            String sortBy = "distance";

            CourseSpot courseSpot = new CourseSpot();
            courseSpot.setCourse(testCourse);

            List<CourseSpot> courseSpots = List.of(courseSpot);
            given(courseSpotRepository.findBySpotIdOrderByCourse_DistanceAsc(spotId)).willReturn(courseSpots);
            given(courseMapper.courseToCourseListResponse(any(Course.class))).willReturn(testCourseListResponse);

            // When
            List<CourseListResponse> result = courseService.findSpotCourseList(spotId, sortBy);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            verify(courseSpotRepository, times(1)).findBySpotIdOrderByCourse_DistanceAsc(spotId);
        }

        @Test
        @DisplayName("난이도순으로 정렬된 코스 리스트를 반환한다")
        void findSpotCourseList_sortByDifficulty_returnsOrderedList() {
            // Given
            Long spotId = 1L;
            String sortBy = "difficulty";

            CourseSpot courseSpot = new CourseSpot();
            courseSpot.setCourse(testCourse);

            List<CourseSpot> courseSpots = List.of(courseSpot);
            given(courseSpotRepository.findBySpotIdOrderByCourse_DifficultyAsc(spotId)).willReturn(courseSpots);
            given(courseMapper.courseToCourseListResponse(any(Course.class))).willReturn(testCourseListResponse);

            // When
            List<CourseListResponse> result = courseService.findSpotCourseList(spotId, sortBy);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            verify(courseSpotRepository, times(1)).findBySpotIdOrderByCourse_DifficultyAsc(spotId);
        }
    }

    @Nested
    @DisplayName("findTop5CourseList 메서드")
    class FindTop5CourseListTest {

        @Test
        @DisplayName("인기 코스 Top5를 반환한다")
        void findTop5CourseList_returnsTop5Courses() {
            // Given
            List<Course> top5Courses = List.of(testCourse);
            CourseTop5Response top5Response = new CourseTop5Response();
            top5Response.setCourseId(1L);
            top5Response.setName("테스트 코스");

            given(courseRepository.findTopNByRunCount(any(PageRequest.class))).willReturn(top5Courses);
            given(courseMapper.courseToCourseTop5Response(any(Course.class))).willReturn(top5Response);

            // When
            List<CourseTop5Response> result = courseService.findTop5CourseList();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            verify(courseRepository, times(1)).findTopNByRunCount(any(PageRequest.class));
        }
    }

    @Nested
    @DisplayName("createCourseFavorite 메서드")
    class CreateCourseFavoriteTest {

        @Test
        @DisplayName("코스와 멤버가 존재하고 좋아요가 없으면 성공적으로 생성한다")
        void createCourseFavorite_withValidData_createsSuccessfully() {
            // Given
            Long courseId = 1L;
            Long memberId = 1L;

            given(courseRepository.findById(courseId)).willReturn(Optional.of(testCourse));
            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(favoriteRepository.existsByMemberAndCourse(testMember, testCourse)).willReturn(false);

            // When
            courseService.createCourseFavorite(courseId, memberId);

            // Then
            verify(favoriteRepository, times(1)).save(any(Favorite.class));
        }

        @Test
        @DisplayName("코스가 존재하지 않으면 예외가 발생한다")
        void createCourseFavorite_courseNotFound_throwsException() {
            // Given
            Long courseId = 999L;
            Long memberId = 1L;

            given(courseRepository.findById(courseId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courseService.createCourseFavorite(courseId, memberId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.COURSE_NOT_FOUND);
        }

        @Test
        @DisplayName("멤버가 존재하지 않으면 예외가 발생한다")
        void createCourseFavorite_memberNotFound_throwsException() {
            // Given
            Long courseId = 1L;
            Long memberId = 999L;

            given(courseRepository.findById(courseId)).willReturn(Optional.of(testCourse));
            given(memberRepository.findById(memberId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courseService.createCourseFavorite(courseId, memberId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.MEMBER_NOT_FOUND);
        }

        @Test
        @DisplayName("이미 좋아요가 존재하면 예외가 발생한다")
        void createCourseFavorite_alreadyExists_throwsException() {
            // Given
            Long courseId = 1L;
            Long memberId = 1L;

            given(courseRepository.findById(courseId)).willReturn(Optional.of(testCourse));
            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(favoriteRepository.existsByMemberAndCourse(testMember, testCourse)).willReturn(true);

            // When & Then
            assertThatThrownBy(() -> courseService.createCourseFavorite(courseId, memberId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.FAVORITE_EXIST);
        }
    }

    @Nested
    @DisplayName("removeCourseFavorite 메서드")
    class RemoveCourseFavoriteTest {

        @Test
        @DisplayName("좋아요가 존재하면 성공적으로 삭제한다")
        void removeCourseFavorite_withValidData_removesSuccessfully() {
            // Given
            Long courseId = 1L;
            Long memberId = 1L;
            Favorite favorite = new Favorite();

            given(courseRepository.findById(courseId)).willReturn(Optional.of(testCourse));
            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(favoriteRepository.findByMemberAndCourse(testMember, testCourse)).willReturn(Optional.of(favorite));

            // When
            courseService.removeCourseFavorite(courseId, memberId);

            // Then
            verify(favoriteRepository, times(1)).delete(favorite);
        }

        @Test
        @DisplayName("좋아요가 존재하지 않으면 예외가 발생한다")
        void removeCourseFavorite_favoriteNotFound_throwsException() {
            // Given
            Long courseId = 1L;
            Long memberId = 1L;

            given(courseRepository.findById(courseId)).willReturn(Optional.of(testCourse));
            given(memberRepository.findById(memberId)).willReturn(Optional.of(testMember));
            given(favoriteRepository.findByMemberAndCourse(testMember, testCourse)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courseService.removeCourseFavorite(courseId, memberId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.FAVORITE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("createCourse 메서드")
    class CreateCourseTest {

        @Test
        @DisplayName("새로운 코스를 성공적으로 생성한다")
        void createCourse_withValidData_createsSuccessfully() {
            // Given
            Course newCourse = new Course();
            newCourse.setName("새 코스");
            newCourse.setGpxpath("/test/new.gpx");

            GpxResult gpxResult = new GpxResult(
                    new ArrayList<>(),  // path
                    new ArrayList<>(),  // segLen
                    new ArrayList<>(),  // prefix
                    10.0,               // distanceKm
                    90,                 // movingMinutes
                    100.0,              // totalAscent
                    5.0,                // maxGrade
                    2                   // difficulty
            );

            given(courseRepository.findByName("새 코스")).willReturn(Optional.empty());
            given(gpxUtil.analyzeGpx(anyString())).willReturn(gpxResult);
            given(courseRepository.save(any(Course.class))).willReturn(newCourse);
            given(spotAPIClient.callAreaBasedAPI(anyInt(), anyInt(), anyInt())).willReturn(new ArrayList<>());
            given(gpxUtil.pickSpots(any(Course.class), any(GpxResult.class), anyList())).willReturn(new ArrayList<>());
            given(courseMapper.courseToCourseResponse(any(Course.class))).willReturn(testCourseResponse);

            // When
            CourseResponse result = courseService.createCourse(newCourse);

            // Then
            assertThat(result).isNotNull();
            verify(courseRepository, times(1)).save(any(Course.class));
        }

        @Test
        @DisplayName("이미 존재하는 코스 이름이면 예외가 발생한다")
        void createCourse_duplicateName_throwsException() {
            // Given
            Course newCourse = new Course();
            newCourse.setName("테스트 코스");

            given(courseRepository.findByName("테스트 코스")).willReturn(Optional.of(testCourse));

            // When & Then
            assertThatThrownBy(() -> courseService.createCourse(newCourse))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.COURSE_NAME_EXIST);
        }
    }

    @Nested
    @DisplayName("removeCourse 메서드")
    class RemoveCourseTest {

        @Test
        @DisplayName("존재하는 코스를 성공적으로 삭제한다")
        void removeCourse_withValidId_removesSuccessfully() {
            // Given
            Long courseId = 1L;
            given(courseRepository.findById(courseId)).willReturn(Optional.of(testCourse));

            // When
            courseService.removeCourse(courseId);

            // Then
            verify(courseRepository, times(1)).delete(testCourse);
        }

        @Test
        @DisplayName("존재하지 않는 코스 삭제 시 예외가 발생한다")
        void removeCourse_courseNotFound_throwsException() {
            // Given
            Long courseId = 999L;
            given(courseRepository.findById(courseId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> courseService.removeCourse(courseId))
                    .isInstanceOf(BusinessLogicException.class)
                    .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.COURSE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("syncCourses 메서드")
    class SyncCoursesTest {

        @Test
        @DisplayName("외부 API에서 코스 데이터를 가져와 저장한다")
        void syncCourses_savesCoursesFromAPI() {
            // Given
            CourseItem courseItem = new CourseItem();
            courseItem.setCrsKorNm("API 코스");

            List<CourseItem> courseItems = List.of(courseItem);
            given(courseAPIClient.callCourseAPI(anyInt(), anyInt())).willReturn(courseItems);
            given(courseMapper.courseItemToCourse(any(CourseItem.class))).willReturn(testCourse);

            // When
            courseService.syncCourses();

            // Then
            verify(courseRepository, times(1)).save(any(Course.class));
        }
    }

    @Nested
    @DisplayName("getCourseImageUrl 메서드")
    class GetCourseImageUrlTest {

        @Test
        @DisplayName("코스에 스팟이 있으면 첫 번째 스팟의 이미지 URL을 반환한다")
        void getCourseImageUrl_withSpots_returnsSpotImageUrl() {
            // Given
            CourseSpotResponse spotResponse = new CourseSpotResponse();
            spotResponse.setSpotId(1L);
            List<CourseSpotResponse> spotList = List.of(spotResponse);

            SpotItem spotItem = new SpotItem();
            spotItem.setFirstimage("https://example.com/image.jpg");

            given(spotAPIClient.callContentIdBasedAPI(1L)).willReturn(spotItem);

            // When
            String result = courseService.getCourseImageUrl(spotList);

            // Then
            assertThat(result).isEqualTo("https://example.com/image.jpg");
        }

        @Test
        @DisplayName("코스에 스팟이 없으면 기본 이미지 URL을 반환한다")
        void getCourseImageUrl_withoutSpots_returnsDefaultImageUrl() {
            // Given
            List<CourseSpotResponse> emptySpotList = new ArrayList<>();

            // When
            String result = courseService.getCourseImageUrl(emptySpotList);

            // Then
            assertThat(result).isEqualTo("https://dallinday-bucket.s3.ap-northeast-2.amazonaws.com/images/default_course.jpg");
        }
    }
}
