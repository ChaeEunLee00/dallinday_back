package com.by.dallinday.common.auth.userinfo;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getImageUrl();
}
