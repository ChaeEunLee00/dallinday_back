package com.by.dallinday.course;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.course.tourAPI.CourseAPIClient;
import com.by.dallinday.course.tourAPI.CourseItem;
import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.common.gpx.DistanceUtil;
import com.by.dallinday.common.gpx.GpxParser;
import com.by.dallinday.course.dto.CourseResponse;
import com.by.dallinday.favorite.Favorite;
import com.by.dallinday.favorite.FavoriteRepository;
import com.by.dallinday.member.Member;
import com.by.dallinday.member.MemberRepository;
import com.by.dallinday.spot.tourAPI.SpotAPIClient;
import com.by.dallinday.spot.tourAPI.SpotItem;
import io.jenetics.jpx.WayPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    // 코스 조회
    public CourseResponse findCourse(String courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));

        return courseMapper.courseToCourseResponse(course);
    }

    // 관광지 별 코스 리스트 조회
    public List<CourseListResponse> findSpotCourseList(Long spotId) {
        // spot 좌표 가져오기
        SpotItem spotItem = spotAPIClient.callContentIdBasedAPI(spotId);
        double spotLat = spotItem.getMapy();
        double spotLon = spotItem.getMapx();

        double radius = 10000.0; // 10km 기준

        List<Course> allCourses = courseRepository.findAll();
        List<Course> matchedCourses = new ArrayList<>();

        for (Course course : allCourses) {
            try {
                List<WayPoint> points = GpxParser.extractCoordinates(course.getGpxpath());

                boolean withinRadius = points.stream().anyMatch(point ->
                        DistanceUtil.haversine(
                                point.getLatitude().doubleValue(),
                                point.getLongitude().doubleValue(),
                                spotLat,
                                spotLon
                        ) < radius
                );

                if (withinRadius) {
                    matchedCourses.add(course);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Course -> CourseListResponse 맵핑
        return matchedCourses.stream()
                .map(course -> courseMapper.courseCourseListResponse(course))
                .toList();
    }

    // 테마 별 코스 리스트 조회
    public Course findThemeCourseList() {
        return new Course();
    }

    public void createCourseFavorite(String courseId, Long memberId) {
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

    public void removeCourseFavorite(String courseId, Long memberId) {
        // course와 member가 존재하는지 확인
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Favorite favorite = favoriteRepository.findByMemberAndCourse(member, course)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.FAVORITE_NOT_FOUND));

        favoriteRepository.delete(favorite);
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
