package com.by.dallinday.course;

import com.by.dallinday.course.dto.CourseListResponse;
import org.springframework.stereotype.Component;

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
}
