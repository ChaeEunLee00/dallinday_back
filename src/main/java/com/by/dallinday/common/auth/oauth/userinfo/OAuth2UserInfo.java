package com.by.dallinday.common.auth.oauth.userinfo;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getImageUrl();
}
