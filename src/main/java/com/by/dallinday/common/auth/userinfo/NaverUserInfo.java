package com.by.dallinday.common.auth.userinfo;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private String provider;
    private Map<String, Object> attributes; // getAttributes()

    public NaverUserInfo(String provider, Map<String, Object> attributes) {
        this.provider = provider;
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return this.provider;
    }

    @Override
    public String getProviderId() {
        Map<String, Object> response = getStringObjectMap();
        if (response == null) return null;

        return (String) response.get("id");
    }
    @Override
    public String getEmail() {
        Map<String, Object> response = getStringObjectMap();
        if (response == null) return null;

        return (String) response.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> response = getStringObjectMap();
        if (response == null) return null;

        return (String) response.get("name");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> response = getStringObjectMap();
        if (response == null) return null;

        return (String) response.get("profile_image");
    }

    private Map<String, Object> getStringObjectMap() {
        return (Map<String, Object>) attributes.get("response");
    }
}
