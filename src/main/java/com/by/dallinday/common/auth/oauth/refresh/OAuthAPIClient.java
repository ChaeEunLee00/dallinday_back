package com.by.dallinday.common.auth.oauth.refresh;

import com.by.dallinday.common.auth.util.EncryptUtil;
import com.by.dallinday.common.exception.BusinessLogicException;
import com.by.dallinday.common.exception.ExceptionCode;
import com.by.dallinday.member.Member;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAPIClient {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    private final EncryptUtil encryptUtil;
    private final OAuthRefreshTokenRepository oAuthRefreshTokenRepository;

    public void revoke(Member member){
        String provider = member.getProvider();

        OAuthRefreshToken saved = oAuthRefreshTokenRepository.findByMember_MemberId(member.getMemberId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.OAUTH_REFRESH_TOKEN_NOT_FOUND));
        String refreshToken = encryptUtil.decrypt(saved.getEncryptedRefreshToken());

        if ("google".equals(provider)) revokeGoogle(refreshToken);
        else if ("kakao".equals(provider)) revokeKaKao(refreshToken);
        else if ("naver".equals(provider)) revokeNaver(refreshToken);
    }

    /** Google: refresh_token -> access_token -> /revoke(token=access_token) */
    private void revokeGoogle(String refreshToken) {
        // 리프레시 토큰을 통해 액세스 토큰 얻기
        String accessToken = fetchAccessTokenGoogle(refreshToken);

        // 액세스 토큰으로 연결 철회
        postForm("https://oauth2.googleapis.com/revoke?token="+accessToken, new LinkedHashMap<>(), null);
        log.info("[Google] revoke OK");
    }

    /** Kakao: refresh_token -> access_token -> unlink (Bearer access_token) */
    private void revokeKaKao(String refreshToken) {
        // 리프레시 토큰을 통해 액세스 토큰 얻기
        String accessToken = fetchAccessTokenKakao(refreshToken);

        // 액세스 토큰으로 연결 철회
        Map<String, String> header = new LinkedHashMap<>();
        header.put("Authorization", "Bearer " + accessToken);
        postForm("https://kapi.kakao.com/v1/user/unlink", new LinkedHashMap<>(), header);
        log.info("[Kakao] unlink OK (via user access token)");
    }

    /** Naver: refresh_token -> access_token -> grant_type=delete */
    private void revokeNaver(String refreshToken) {
        // 리프레시 토큰을 통해 액세스 토큰 얻기
        String accessToken = fetchAccessTokenNaver(refreshToken);

        // 액세스 토큰으로 연결 철회
        String url = "https://nid.naver.com/oauth2.0/token?"
                + "grant_type=" + "delete"
                + "&client_id=" + naverClientId
                + "&client_secret=" + naverClientSecret
                + "&refresh_token=" + refreshToken
                + "&access_token=" + "NAVER";
        postForm(url, new LinkedHashMap<>(), null);
        log.info("[Naver] delete OK");
    }

    // ---------- Provider token fetchers ----------

    private String fetchAccessTokenGoogle(String refreshToken) {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("grant_type", "refresh_token");
        form.put("client_id", googleClientId);
        form.put("client_secret", googleClientSecret);
        form.put("refresh_token", refreshToken);

        JsonNode jsonNode = postForm("https://oauth2.googleapis.com/token", form, null);
        String accessToken = jsonNode.path("access_token").asText(null);
        if (accessToken == null) throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        return accessToken;
    }

    private String fetchAccessTokenKakao(String refreshToken) {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("grant_type", "refresh_token");
        form.put("client_id", kakaoClientId);
        form.put("client_secret", kakaoClientSecret);
        form.put("refresh_token", refreshToken);

        JsonNode jsonNode = postForm("https://kauth.kakao.com/oauth/token", form, null);
        String accessToken = jsonNode.path("access_token").asText(null);
        if (accessToken == null) throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        return accessToken;
    }

    private String fetchAccessTokenNaver(String refreshToken) {
        String url = "https://nid.naver.com/oauth2.0/token?"
                + "&grant_type=" + "refresh_token"
                + "&client_id=" + naverClientId
                + "&client_secret=" + naverClientSecret
                + "&refresh_token=" + refreshToken;

        JsonNode jsonNode = postForm(url, new LinkedHashMap<>(), null);
        String accessToken = jsonNode.path("access_token").asText(null);
        if (accessToken == null) throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        return accessToken;
    }

    // ---------- Low-level HTTP (HttpURLConnection) ----------

    /** x-www-form-urlencoded POST. headers에 Authorization 등 추가 가능. 성공/에러 본문을 문자열로 반환 */
    private JsonNode postForm(String url, Map<String, String> form, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            byte[] postData = encodeForm(form).getBytes(StandardCharsets.UTF_8);

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            if (headers != null) {
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    conn.setRequestProperty(e.getKey(), e.getValue());
                }
            }
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(postData.length);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData);
            }

            // response
            int code = conn.getResponseCode();
            String result = readBody(conn, code);

            log.info("[HTTP POST] {} -> status={} body={}", url, code, result);
            if (code >= 200 && code < 300) {
                // 성공: JSON 파싱
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readTree(result);
            } else {
                // 실패: 응답 바디와 코드 로그만 남기고 예외
                throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
            }

        } catch (BusinessLogicException e) {
            throw e;
        } catch (Exception e) {
            log.info("HTTP POST failed: url={}, err={}", url, e.toString());
            throw new BusinessLogicException(ExceptionCode.EXTERNAL_API_ERROR);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private static String readBody(HttpURLConnection conn, int code) throws IOException {
        InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        if (is == null) return "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) sb.append(line);
            return sb.toString();
        }
    }

    private static String encodeForm(Map<String, String> form) {
        if (form == null || form.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : form.entrySet()) {
            if (sb.length() > 0) sb.append('&');
            sb.append(urlEncode(e.getKey())).append('=').append(urlEncode(e.getValue()));
        }
        return sb.toString();
    }

    private static String urlEncode(String s) {
        try { return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8.name()); }
        catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
    }
}
