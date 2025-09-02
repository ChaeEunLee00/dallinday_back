package com.by.dallinday.common.geo;

import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class GeoService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoApiKey;

    public GeoResponse coordToAddress(double lon, double lat) {
        try {
            String apiUrl = "https://dapi.kakao.com/v2/local/geo/coord2address.json?" +
                    "x=" + lon +
                    "&y=" + lat +
                    "&input_coord=WGS84";

            // URL & Connection
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + kakaoApiKey);

            // 응답 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            // JSON 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(sb.toString());

            JsonNode doc = root.get("documents").get(0);
            String addr = doc.has("address") && !doc.get("address").asText().equals("null")
                    ? doc.get("address").get("address_name").asText()
                    : null;

            String road = doc.has("road_address") && !doc.get("road_address").asText().equals("null")
                    ? doc.get("road_address").get("address_name").asText()
                    : null;

            return new GeoResponse(addr, road);

        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        }
    }
}
