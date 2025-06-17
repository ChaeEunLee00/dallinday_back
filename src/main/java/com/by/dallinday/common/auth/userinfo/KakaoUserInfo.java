package com.by.dallinday.common.auth.userinfo;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

    private String provider;
    private Map<String, Object> attributes; // getAttributes()

    public KakaoUserInfo(String provider, Map<String, Object> attributes) {
        this.provider = provider;
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return this.provider;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("profile_nickname");
    }

    @Override
    public String getName() {
        return (String) attributes.get("profile_nickname");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("profile_image");
    }


}
