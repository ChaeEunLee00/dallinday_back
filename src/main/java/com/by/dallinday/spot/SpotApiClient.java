package com.by.dallinday.spot;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Component
public class SpotApiClient {

    @Value("${open-api.tour-api.service-key}")
    private String serviceKey;

    public String callSpotAPI() throws Exception {
        try {
            String result = "";

            URL url = new URL("http://apis.data.go.kr/B551011/KorService2/areaBasedList2?" +
                    "serviceKey=" + serviceKey +
                    "&numOfRows=" + "10" +
                    "&pageNo=" + "1" +
                    "&MobileOS=" + "ETC" +
                    "&MobileApp=" + "AppTest" +
                    "&_type=" + "json" +
                    "&arrange=" + "C" +
                    "&contentTypeId=" + "12" +
                    "&areaCode=" + "7");

            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")); // url.openStream()내부적으로 HttpURLConnection을 생성해 HTTP GET 요청을 보냄 InputStream만 쉽게 꺼내주는 단축 메서드
            result = bf.readLine();

            return result;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BusinessLogicException(ExceptionCode.DATABASE_ERROR);
        }
    }
}
