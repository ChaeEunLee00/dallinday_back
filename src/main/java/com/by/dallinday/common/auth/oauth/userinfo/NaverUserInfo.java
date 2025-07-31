package com.by.dallinday.common.auth.oauth.userinfo;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private String provider;
    private Map<String, Object> response; // getAttributes()

    public NaverUserInfo(String provider, Map<String, Object> attributes) {
        this.provider = provider;
        this.response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return this.provider;
    }

    @Override
    public String getProviderId() {
        return (String) response.get("id");
    }
    @Override
    public String getEmail() {
        return (String) response.get("email");
    }

    @Override
    public String getName() {
        return (String) response.get("name");
    }

    @Override
    public String getImageUrl() {
        return (String) response.get("profile_image");
    }
}
