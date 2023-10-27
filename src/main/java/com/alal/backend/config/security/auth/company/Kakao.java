package com.alal.backend.config.security.auth.company;



import com.alal.backend.config.security.auth.OAuth2UserInfo;
import com.alal.backend.domain.entity.user.Provider;

import java.util.Map;

public class Kakao extends OAuth2UserInfo {
    
    public Kakao(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getLogin() {
        return null;
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        if (properties == null) {
            return null;
        }

        return (String) properties.get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");
        if (properties == null) {
            return null;
        }
        return (String) properties.get("email");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        if (properties == null) {
            return null;
        }

        return (String) properties.get("thumbnail_image");
    }

    @Override
    public String getProvider(){
        return Provider.kakao.toString();
    }
}
