package com.alal.backend.domain.entity.user;


import com.alal.backend.domain.entity.time.DefaultTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Table(name="token")
@Entity
@Builder
@AllArgsConstructor
public class Token extends DefaultTime {

    @Id
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    public Token(){}

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @Builder
    public Token(String userEmail, String refreshToken) {
        this.userEmail = userEmail;
        this.refreshToken = refreshToken;
    }
}
