package com.by.dallinday.spot.tourAPI;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SpotAPIClient {

    @Value("${open-api.tour-api.service-key}")
    private String serviceKey;

    private final String MobileOS = "AND";
    private final String MobileApp = URLEncoder.encode("달린데이", StandardCharsets.UTF_8);

    public List<SpotItem> callAreaBasedAPI(int areaCode, int numOfRows, int pageNo) {
        try {
            String result = "";

            URL url = new URL("https://apis.data.go.kr/B551011/KorService2/areaBasedList2?" +
                    "serviceKey=" + serviceKey +
                    "&numOfRows=" + numOfRows +
                    "&pageNo=" + pageNo +
                    "&MobileOS=" + MobileOS +
                    "&MobileApp=" + MobileApp +
                    "&_type=" + "json" +
                    "&arrange=" + "C" +
                    "&contentTypeId=" + "12" +
                    "&areaCode=" + areaCode);

            // url.openStream()
            // : InputStream 만 쉽게 꺼내주는 단축 메서드
            // : 내부적으로 HttpURLConnection을 생성해 HTTP GET 요청을 보냄
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result);  // 전체 JSON 파싱
            JsonNode itemArray = rootNode.findValue("item"); // 중첩 구조에서 item 배열 추출

            // item 배열을 SpotItem 객체 배열로 변환
            if(itemArray == null) return new ArrayList<>();
            return Arrays.asList(mapper.treeToValue(itemArray, SpotItem[].class));
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        }
    }

    public List<SpotItem> callLocationBasedAPI(double mapX, double mapY, int areaCode, int radius, int numOfRows, int pageNo) {
        try {
            String result = "";

            URL url = new URL("https://apis.data.go.kr/B551011/KorService2/locationBasedList2?" +
                    "serviceKey=" + serviceKey +
                    "&numOfRows=" + numOfRows +
                    "&pageNo=" + pageNo +
                    "&MobileOS=" + MobileOS +
                    "&MobileApp=" + MobileApp +
                    "&_type=" + "json" +
                    "&arrange=" + "E" +
                    "&mapX=" + mapX +
                    "&mapY=" + mapY +
                    "&radius=" + radius +
                    "&contentTypeId=" + "12" +
                    "&areaCode=" + areaCode);

            // url.openStream()
            // : InputStream 만 쉽게 꺼내주는 단축 메서드
            // : 내부적으로 HttpURLConnection을 생성해 HTTP GET 요청을 보냄
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result);  // 전체 JSON 파싱
            JsonNode itemArray = rootNode.findValue("item"); // 중첩 구조에서 item 배열 추출

            // item 배열을 SpotItem 객체 배열로 변환
            if(itemArray == null) return new ArrayList<>();
            return Arrays.asList(mapper.treeToValue(itemArray, SpotItem[].class));
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        }
    }

    // 특정 관광지 정보
    public SpotItem callContentIdBasedAPI(Long spotId) {
        System.out.println("spotId = " + spotId);
        try {
            String result = "";

            URL url = new URL("https://apis.data.go.kr/B551011/KorService2/detailCommon2?" +
                    "serviceKey=" + serviceKey +
                    "&MobileOS=" + MobileOS +
                    "&MobileApp=" + MobileApp +
                    "&_type=" + "json" +
                    "&contentId=" + spotId);

            // url.openStream()
            // : InputStream 만 쉽게 꺼내주는 단축 메서드
            // : 내부적으로 HttpURLConnection을 생성해 HTTP GET 요청을 보냄
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result);  // 전체 JSON 파싱
            JsonNode itemArray = rootNode.findValue("item"); // 중첩 구조에서 item 배열 추출

            // item 배열을 SpotItem 객체 배열로 변환
            return Arrays.asList(mapper.treeToValue(itemArray, SpotItem[].class)).get(0);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        }
    }

    public SpotCommon callCommonInfoAPI(Long spotId) {
        try {
            String result = "";

            URL url = new URL("https://apis.data.go.kr/B551011/KorService2/detailCommon2?" +
                    "serviceKey=" + serviceKey +
                    "&numOfRows=" + 10 +
                    "&pageNo=" + 1 +
                    "&MobileOS=" + MobileOS +
                    "&MobileApp=" + MobileApp +
                    "&_type=" + "json" +
                    "&contentId=" + spotId);

            // url.openStream()
            // : InputStream 만 쉽게 꺼내주는 단축 메서드
            // : 내부적으로 HttpURLConnection을 생성해 HTTP GET 요청을 보냄
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result);  // 전체 JSON 파싱
            JsonNode itemArray = rootNode.findValue("item"); // 중첩 구조에서 item 배열 추출

            // item 배열을 SpotCommon 객체 배열로 변환
            return mapper.treeToValue(itemArray.get(0), SpotCommon.class);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        }
    }

    public SpotDetail callDetailInfoAPI(Long spotId) {
        try {
            String result = "";

            URL url = new URL("https://apis.data.go.kr/B551011/KorService2/detailIntro2?" +
                    "serviceKey=" + serviceKey +
                    "&numOfRows=" + 10 +
                    "&pageNo=" + 1 +
                    "&MobileOS=" + MobileOS +
                    "&MobileApp=" + MobileApp +
                    "&_type=" + "json" +
                    "&contentId=" + spotId +
                    "&contentTypeId=" + "12");

            // url.openStream()
            // : InputStream 만 쉽게 꺼내주는 단축 메서드
            // : 내부적으로 HttpURLConnection을 생성해 HTTP GET 요청을 보냄
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result);  // 전체 JSON 파싱
            JsonNode itemArray = rootNode.findValue("item"); // 중첩 구조에서 item 배열 추출

            // item 배열을 SpotItem 객체 배열로 변환
            return mapper.treeToValue(itemArray.get(0), SpotDetail.class);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        }
    }

    public List<SpotRepeat> callRepeatInfoAPI(Long spotId) {
        try {
            String result = "";

            URL url = new URL("https://apis.data.go.kr/B551011/KorService2/detailInfo2?" +
                    "serviceKey=" + serviceKey +
                    "&numOfRows=" + 10 +
                    "&pageNo=" + 1 +
                    "&MobileOS=" + MobileOS +
                    "&MobileApp=" + MobileApp +
                    "&_type=" + "json" +
                    "&contentId=" + spotId +
                    "&contentTypeId=" + "12");

            // url.openStream()
            // : InputStream 만 쉽게 꺼내주는 단축 메서드
            // : 내부적으로 HttpURLConnection을 생성해 HTTP GET 요청을 보냄
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result);  // 전체 JSON 파싱
            JsonNode itemArray = rootNode.findValue("item"); // 중첩 구조에서 item 배열 추출

            // item 배열을 SpotItem 객체 배열로 변환
            if(itemArray == null) return new ArrayList<>();
            return Arrays.asList(mapper.treeToValue(itemArray, SpotRepeat[].class));
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        }
    }
}
