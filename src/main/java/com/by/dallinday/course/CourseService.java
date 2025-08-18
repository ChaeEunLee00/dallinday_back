package com.by.dallinday.course;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.common.gpx.GpxResult;
import com.by.dallinday.course.dto.CourseSpotResponse;
import com.by.dallinday.course.tourAPI.CourseAPIClient;
import com.by.dallinday.course.tourAPI.CourseItem;
import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.common.gpx.GpxUtil;
import com.by.dallinday.course.dto.CourseResponse;
import com.by.dallinday.coursespot.CourseSpot;
import com.by.dallinday.coursespot.CourseSpotRepository;
import com.by.dallinday.favorite.Favorite;
import com.by.dallinday.favorite.FavoriteRepository;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import com.by.dallinday.spot.tourAPI.SpotAPIClient;
import com.by.dallinday.spot.tourAPI.SpotItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseMapper courseMapper;

    private final SpotAPIClient spotAPIClient;
    private final CourseAPIClient courseAPIClient;

    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final CourseSpotRepository courseSpotRepository;

    private final GpxUtil gpxUtil;

    // 코스 조회
    public CourseResponse findCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));

        // 응답 형식에 맞춰 변환
        CourseResponse courseResponse = courseMapper.courseToCourseResponse(course);
        List<CourseSpotResponse> courseSpotResponses = course.getSpots().stream()
                .map(courseSpot -> spotAPIClient.callContentIdBasedAPI(courseSpot.getSpotId()))
                .map(spotItem -> courseMapper.spotItemToCourseSpotResponse(spotItem))
                .toList();
        courseResponse.setSpots(courseSpotResponses);

        return courseMapper.courseToCourseResponse(course);
    }

    // 관광지 별 코스 리스트 조회
    public List<CourseListResponse> findSpotCourseList(Long spotId) {
        List<CourseSpot> courseSpots = courseSpotRepository.findBySpotId(spotId);
        List<Course> courses = courseSpots.stream()
                .map(CourseSpot::getCourse)
                .distinct()
                .toList();

        return courses.stream().map(course -> {
            CourseListResponse courseListResponse = courseMapper.courseToCourseListResponse(course);
            List<CourseSpotResponse> courseSpotResponses = course.getSpots().stream()
                    .map(courseSpot -> spotAPIClient.callContentIdBasedAPI(courseSpot.getSpotId()))
                    .map(spotItem -> courseMapper.spotItemToCourseSpotResponse(spotItem))
                    .toList();

            courseListResponse.setSpots(courseSpotResponses);
            return courseListResponse;
        }).toList();
    }

    public void createCourseFavorite(Long courseId, Long memberId) {
        // course와 member가 존재하는지 확인
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // 이미 좋아요 하였는지 확인
        boolean alreadyFavorite = favoriteRepository.existsByMemberAndCourse(member, course);
        if (alreadyFavorite) {
            throw new BusinessLogicException(ExceptionCode.FAVORITE_EXIST);
        }

        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setCourse(course);
        favorite.setFavoriteAt(LocalDateTime.now());

        favoriteRepository.save(favorite);
    }

    public void removeCourseFavorite(Long courseId, Long memberId) {
        // course와 member가 존재하는지 확인
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Favorite favorite = favoriteRepository.findByMemberAndCourse(member, course)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.FAVORITE_NOT_FOUND));

        favoriteRepository.delete(favorite);
    }

    // 코스 생성 (관리자용)
    public CourseResponse createCourse(Course course) {
        // 이미 존재하는 코스 이름인지 확인
        if (courseRepository.findByName(course.getName()).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.COURSE_NAME_EXIST);
        }

        // gpx파일 분석 - 거리 시간 난이도
        GpxResult result = gpxUtil.analyzeGpx(course.getGpxpath());

        // 분석값을 코스 엔티티에 채움
        course.setDistance(result.getDistanceKm()); // km
        course.setDuration(result.getMovingMinutes()); // 분
        course.setDifficulty(result.getDifficulty()); // 1~3
        course.setCreatedAt(LocalDateTime.now()); // 생성시간
        course.setModifiedAt(LocalDateTime.now()); // 수정시간

        // 코스 우선 저장 (코스 PK 필요)
        Course savedCourse = courseRepository.save(course);

        // spot 203개와 비교하여 장소 목록을 저장
        List<SpotItem> spots = spotAPIClient.callAreaBasedAPI(7,300, 1);
        List<CourseSpot> courseSpots = gpxUtil.pickSpots(savedCourse, result, spots);
        System.out.println(courseSpots.size());

        // 코스-스팟 연결정보 저장
        savedCourse.setSpots(courseSpots);

        // 응답 형식에 맞춰 변환
        CourseResponse courseResponse = courseMapper.courseToCourseResponse(course);
        List<CourseSpotResponse> courseSpotResponses = courseSpots.stream()
                .map(courseSpot -> spotAPIClient.callContentIdBasedAPI(courseSpot.getSpotId()))
                .map(spotItem -> courseMapper.spotItemToCourseSpotResponse(spotItem))
                .toList();
        courseResponse.setSpots(courseSpotResponses);

        return courseResponse;
    }

    // 코스 삭제 (관리자용)
    public void removeCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));

        courseRepository.delete(course);
    }

    // TourAPI를 통해 가져온 코스 데이터 저장 및 동기화
    public void syncCourses() {
        int page = 1, size = 10; // 가져올 데이터 수
        List<CourseItem> courseItems = courseAPIClient.callCourseAPI(size, page);

        for (CourseItem item : courseItems) {
            Course newCourse = courseMapper.courseItemToCourse(item);
            courseRepository.save(newCourse);
        }
    }
}
