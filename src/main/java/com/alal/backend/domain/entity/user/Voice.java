package com.alal.backend.domain.entity.user;

import com.alal.backend.payload.request.user.FlaskVoiceRequest;
import com.alal.backend.domain.dto.response.FlaskResponse;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Getter
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private String voiceUrl;

    @Column
    private String modelName;

    public static Voice fromDto(FlaskResponse flaskResponse, Long userId, FlaskVoiceRequest flaskRequest) {
        return Voice.builder()
                .userId(userId)
                .voiceUrl(flaskResponse.getResponseMessage())
                .modelName(flaskRequest.getModelName())
                .build();
    }

    public void updateVoiceUrl(FlaskResponse flaskResponse, FlaskVoiceRequest flaskRequest) {
        this.voiceUrl = flaskResponse.getResponseMessage();
        this.modelName = flaskRequest.getModelName();
    }
}
