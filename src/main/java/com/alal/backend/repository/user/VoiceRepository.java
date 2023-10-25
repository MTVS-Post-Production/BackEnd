package com.alal.backend.repository.user;

import com.alal.backend.domain.entity.user.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceRepository extends JpaRepository<Voice, Long> {
    Voice findByUserIdAndModelName(Long userId, String modelName);
}
