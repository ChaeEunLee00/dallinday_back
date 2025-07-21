package com.by.dallinday.spot;

import com.by.dallinday.common.exception.BusinessLogicException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class SpotServiceTest {

    @Autowired
    private SpotService spotService;

    @Test
    void findSpotsByArea_정상호출() {
        // given & when
        try {
            List<SpotItem> items = spotService.findSpotsByArea(7, 10, 1);

            // then
            assertThat(items).isNotNull();
            for (SpotItem item : items) {
                System.out.println(item);
            }

        } catch (BusinessLogicException e) {
            fail("BusinessLogicException 발생: " + e.getMessage());
        } catch (Exception e) {
            fail("Exception 발생: " + e.getMessage());
        }
    }
}

