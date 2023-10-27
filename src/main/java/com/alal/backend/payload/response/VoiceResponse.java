package com.alal.backend.payload.response;

import com.alal.backend.domain.entity.user.Voice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoiceResponse {
    private String voiceUrl;

    public static VoiceResponse fromEntity(Voice createdVoice) {
        return VoiceResponse.builder()
                .voiceUrl(createdVoice.getVoiceUrl())
                .build();
    }
}
