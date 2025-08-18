package com.by.dallinday.course;

import com.by.dallinday.course.dto.CourseSpotResponse;
import com.by.dallinday.course.tourAPI.CourseItem;
import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.course.dto.CourseResponse;
import com.by.dallinday.course.dto.CourseRunResponse;
import com.by.dallinday.coursespot.CourseSpot;
import com.by.dallinday.run.Run;
import com.by.dallinday.spot.tourAPI.SpotItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseMapper {

    public CourseListResponse courseToCourseListResponse(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseListResponse courseListResponse = new CourseListResponse();

        courseListResponse.setCourseId(course.getCourseId());
        courseListResponse.setName(course.getName());
        courseListResponse.setDistance(course.getDistance());
        courseListResponse.setDuration(course.getDuration());
        courseListResponse.setDifficulty(course.getDifficulty());

        return courseListResponse;
    }

    public CourseResponse courseToCourseResponse(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseResponse courseResponse = new CourseResponse();

        courseResponse.setCourseId(course.getCourseId());
        courseResponse.setName(course.getName());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setDistance(course.getDistance());
        courseResponse.setDuration(course.getDuration());
        courseResponse.setDifficulty(course.getDifficulty());
        courseResponse.setGpxpath(course.getGpxpath());
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setModifiedAt(course.getModifiedAt());

        List<CourseRunResponse> courseRunResponsesList = course.getRunList().stream()
                .map(run -> runToCourseRunResponse(run))
                .toList();
        courseResponse.setRunList(courseRunResponsesList);

        return courseResponse;
    }

    public CourseSpotResponse spotItemToCourseSpotResponse(SpotItem spotItem) {
        if ( spotItem == null ) {
            return null;
        }

        CourseSpotResponse courseSpotResponse = new CourseSpotResponse();
        courseSpotResponse.setSpotId(spotItem.getSpotId());
        courseSpotResponse.setName(spotItem.getTitle());
        courseSpotResponse.setMapy(spotItem.getMapy());
        courseSpotResponse.setMapx(spotItem.getMapx());

        return courseSpotResponse;
    }

    private CourseRunResponse runToCourseRunResponse(Run run) {
        if ( run == null ) {
            return null;
        }

        CourseRunResponse courseRunResponse = new CourseRunResponse();

        courseRunResponse.setRunId(run.getRunId());
        courseRunResponse.setMemberId(run.getMember().getMemberId());
        courseRunResponse.setDistance(run.getDistance());
        courseRunResponse.setDuration(run.getDuration());

        return courseRunResponse;
    }

    public Course courseItemToCourse(CourseItem item) {
        if ( item == null ) {
            return null;
        }

        Course course = new Course();

        course.setName(item.getCrsKorNm());
        course.setDescription(item.getCrsSummary()); // 필요시 summary+tourInfo 합치기
        course.setDistance(item.getCrsDstnc());
        course.setDuration(item.getCrsTotlRqrmHour()); // 원 데이터가 '시간'이면 분 단위로 변환 필요
        course.setDifficulty(item.getCrsLevel());
        course.setGpxpath(item.getGpxpath());
        return course;
    }
}
