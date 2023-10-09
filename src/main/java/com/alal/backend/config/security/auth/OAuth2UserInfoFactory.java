package com.alal.backend.config.security.auth;


import com.alal.backend.advice.assertThat.DefaultAssert;
import com.alal.backend.config.security.auth.company.*;
import com.alal.backend.domain.entity.user.Provider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(Provider.google.toString())) {
            return new Google(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.facebook.toString())) {
            return new Facebook(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.github.toString())) {
            return new Github(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.naver.toString())) {
            return new Naver(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.kakao.toString())) {
            return new Kakao(attributes);
        } else {
            DefaultAssert.isAuthentication("해당 oauth2 기능은 지원하지 않습니다.");
        }
        return null;
    }
}
