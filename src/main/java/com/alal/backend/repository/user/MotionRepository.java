package com.alal.backend.repository.user;

import com.alal.backend.domain.entity.user.Motion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotionRepository extends JpaRepository<Motion, Long> {

    @Query("select m.motionGif from Motion m where m.motionGif like %:fileName%")
    List<String> findByMotionGifLikeFileName(String fileName);
}
