package com.by.dallinday.spot;

import com.by.dallinday.common.exception.BusinessLogicException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class SpotApiClientTest {

    @Autowired
    private SpotAPIClient spotApiClient;

    @Test
    void callAreaBasedAPI_정상호출() {
        // given & when
        try {
            List<SpotItem> spotItems = spotApiClient.callAreaBasedAPI(7, 10, 1);

            // then
            assertThat(spotItems).isNotNull();
            System.out.println("API 응답: " + spotItems);
        } catch (BusinessLogicException e) {
            fail("BusinessLogicException 발생: " + e.getMessage());
        } catch (Exception e) {
            fail("Exception 발생: " + e.getMessage());
        }
    }

    @Test
    void callLocationBasedAPI_정상호출() {
        // given & when
        try {
            List<SpotItem> spotItems = spotApiClient.callLocationBasedAPI(129.313,35.537, 7,10000, 10, 1);

            // then
            assertThat(spotItems).isNotNull();
            System.out.println("API 응답: " + spotItems);
        } catch (BusinessLogicException e) {
            fail("BusinessLogicException 발생: " + e.getMessage());
        } catch (Exception e) {
            fail("Exception 발생: " + e.getMessage());
        }
    }
}
