package com.by.dallinday.spot;

import com.by.dallinday.common.exception.BusinessLogicException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class SpotApiClientTest {

    @Autowired
    private SpotApiClient spotApiClient;

    @Test
    void callSpotAPI_정상호출() {
        // given & when
        try {
            String response = spotApiClient.callSpotAPI();

            // then
            assertThat(response).isNotNull();
            System.out.println("API 응답: " + response);
        } catch (BusinessLogicException e) {
            fail("BusinessLogicException 발생: " + e.getMessage());
        } catch (Exception e) {
            fail("Exception 발생: " + e.getMessage());
        }
    }
}
