package com.by.dallinday.common.auth.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

public class UriUtil {

    public static String buildRedirectUri(String scheme, String host, MultiValueMap<String, String> queryParams) {
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .queryParams(queryParams)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();
    }

    public static String buildMyAppRedirectUri(String accessToken, String refreshToken, Long memberId) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);
        queryParams.add("memberId", memberId+"");

        return buildRedirectUri("myapp", "main", queryParams);
    }
}
