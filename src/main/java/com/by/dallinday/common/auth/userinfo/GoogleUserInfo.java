package com.by.dallinday.common.auth.userinfo;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{

    private String provider;
    private Map<String, Object> attributes; // getAttributes()

    public GoogleUserInfo(String provider, Map<String, Object> attributes) {
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
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }


}
