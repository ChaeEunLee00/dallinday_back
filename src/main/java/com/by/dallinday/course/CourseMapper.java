package com.by.dallinday.course;

import com.by.dallinday.course.dto.*;
import com.by.dallinday.course.tourAPI.CourseItem;
import com.by.dallinday.courseSpot.CourseSpot;
import com.by.dallinday.run.Run;
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

        List<CourseSpotResponse> courseSpotResponsesList = course.getCourseSpotList().stream()
                .map(courseSpot -> courseSpotToCourseSpotResponse(courseSpot))
                .toList();
        courseListResponse.setCourseSpotList(courseSpotResponsesList);

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

        List<CourseSpotResponse> courseSpotResponsesList = course.getCourseSpotList().stream()
                .map(courseSpot -> courseSpotToCourseSpotResponse(courseSpot))
                .toList();
        courseResponse.setCourseSpotList(courseSpotResponsesList);

        List<CourseRunResponse> courseRunResponsesList = course.getRunList().stream()
                .map(run -> runToCourseRunResponse(run))
                .toList();
        courseResponse.setRunList(courseRunResponsesList);

        return courseResponse;
    }

    public CourseTop5Response courseToCourseTop5Response(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseTop5Response courseTop5Response = new CourseTop5Response();

        courseTop5Response.setCourseId(course.getCourseId());
        courseTop5Response.setName(course.getName());
        courseTop5Response.setDistance(course.getDistance());
        courseTop5Response.setDuration(course.getDuration());
        courseTop5Response.setRunCount(course.getRunList().size());

        return courseTop5Response;
    }

    public CourseSpotResponse courseSpotToCourseSpotResponse(CourseSpot courseSpot) {
        if ( courseSpot == null ) {
            return null;
        }

        CourseSpotResponse courseSpotResponse = new CourseSpotResponse();
        courseSpotResponse.setSpotId(courseSpot.getSpotId());
        courseSpotResponse.setName(courseSpot.getName());
        courseSpotResponse.setLatitude(courseSpot.getLatitude());
        courseSpotResponse.setLongitude(courseSpot.getLongitude());

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
