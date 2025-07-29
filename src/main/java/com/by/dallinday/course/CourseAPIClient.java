package com.by.dallinday.course;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.spot.SpotItem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CourseAPIClient {

    @Value("${open-api.tour-api.service-key}")
    private String serviceKey;

    public List<CourseItem> callCourseAPI(int numOfRows, int pageNo) {
        try {
            String result = "";

            URL url = new URL("https://apis.data.go.kr/B551011/Durunubi/courseList?" +
                    "serviceKey=" + serviceKey +
                    "&numOfRows=" + numOfRows +
                    "&pageNo=" + pageNo +
                    "&MobileOS=" + "ETC" +
                    "&MobileApp=" + "AppTest" +
                    "&_type=" + "json" +
                    "&crsKorNm=" + URLEncoder.encode("해파랑길", "UTF-8") +
                    "&brdDiv=" + "DNBW");

            // url.openStream()
            // : InputStream 만 쉽게 꺼내주는 단축 메서드
            // : 내부적으로 HttpURLConnection을 생성해 HTTP GET 요청을 보냄
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result);  // 전체 JSON 파싱
            JsonNode itemArray = rootNode.findValue("item"); // 중첩 구조에서 item 배열 추출

            // item 배열을 CourseItem 객체 배열로 변환
            return Arrays.asList(mapper.treeToValue(itemArray, CourseItem[].class));
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        }
    }
}
