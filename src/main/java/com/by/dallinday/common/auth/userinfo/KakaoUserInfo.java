package com.by.dallinday.common.auth.userinfo;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

    private String provider;
    private Map<String, Object> attributes; // getAttributes()
    private Map<String, Object> properties;

    public KakaoUserInfo(String provider, Map<String, Object> attributes) {
        this.provider = provider;
        this.attributes = attributes;
        this.properties = (Map<String, Object>) attributes.get("properties");
    }

    @Override
    public String getProvider() {
        return this.provider;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        return (String) properties.get("nickname");
    }

    @Override
    public String getName() {
        return (String) properties.get("nickname");
    }

    @Override
    public String getImageUrl() {
        return (String) properties.get("profile_image");
    }


}
