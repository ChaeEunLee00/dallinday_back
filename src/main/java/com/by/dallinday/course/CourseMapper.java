package com.by.dallinday.course;

import com.by.dallinday.course.tourAPI.CourseItem;
import com.by.dallinday.course.dto.CourseListResponse;
import com.by.dallinday.course.dto.CourseResponse;
import com.by.dallinday.course.dto.CourseRunResponse;
import com.by.dallinday.run.Run;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseMapper {

    public CourseListResponse courseCourseListResponse(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseListResponse courseListResponse = new CourseListResponse();

        courseListResponse.setCourseId(course.getCourseId());
        courseListResponse.setCrsKorNm(course.getCrsKorNm());
        courseListResponse.setCrsDstnc(course.getCrsDstnc());
        courseListResponse.setCrsTotlRqrmHour(course.getCrsTotlRqrmHour());
        courseListResponse.setCrsLevel(course.getCrsLevel());
        courseListResponse.setCrsTourInfo(course.getCrsTourInfo());

        return courseListResponse;
    }

    public CourseResponse courseToCourseResponse(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseResponse courseResponse = new CourseResponse();

        courseResponse.setCourseId(course.getCourseId());
        courseResponse.setCrsKorNm(course.getCrsKorNm());
        courseResponse.setCrsDstnc(course.getCrsDstnc());
        courseResponse.setCrsTotlRqrmHour(course.getCrsTotlRqrmHour());
        courseResponse.setCrsLevel(course.getCrsLevel());
        courseResponse.setCrsSummary(course.getCrsSummary());
        courseResponse.setCrsTourInfo(course.getCrsTourInfo());
        courseResponse.setGpxpath(course.getGpxpath());
        courseResponse.setCreatedtime(course.getCreatedtime());
        courseResponse.setModifiedtime(course.getModifiedtime());

        List<CourseRunResponse> courseRunResponsesList = course.getRunList().stream()
                .map(run -> runToCourseRunResponse(run))
                .toList();

        courseResponse.setRunList(courseRunResponsesList);
        return courseResponse;
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

        course.setCourseId(item.getCourseId());
        course.setCrsKorNm(item.getCrsKorNm());
        course.setCrsDstnc(item.getCrsDstnc());
        course.setCrsTotlRqrmHour(item.getCrsTotlRqrmHour());
        course.setCrsLevel(item.getCrsLevel());
        course.setCrsSummary(item.getCrsSummary());
        course.setCrsTourInfo(item.getCrsTourInfo());
        course.setGpxpath(item.getGpxpath());
        course.setCreatedtime(item.getCreatedtime());
        course.setModifiedtime(item.getModifiedtime());
        return course;
    }
}
