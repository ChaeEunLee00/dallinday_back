package com.by.dallinday.course;

import com.by.dallinday.common.exception.BusinessLogicException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class CourseAPIClientTest {

    @Autowired
    private CourseAPIClient courseApiClient;

    @Test
    void callCourseAPI_정상호출() {
        // given & when
        try {
            List<CourseItem> courseItems = courseApiClient.callCourseAPI( 10, 1);

            // then
            assertThat(courseItems).isNotNull();
            System.out.println("API 응답: " + courseItems);
        } catch (BusinessLogicException e) {
            fail("BusinessLogicException 발생: " + e.getMessage());
        } catch (Exception e) {
            fail("Exception 발생: " + e.getMessage());
        }
    }
}
