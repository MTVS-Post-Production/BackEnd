package com.alal.backend.repository.user;

import com.alal.backend.domain.entity.user.Motion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MotionRepository extends JpaRepository<Motion, Long> {
    @Query("select m.motionGif from Motion m where m.motionGif like %:message%")
    Page<String> findGifByMotionContaining(@Param("message") String message, Pageable pageable);

    @Query("select m.motionFbx from Motion m where m.motionFbx like %:message%")
    Page<String> findFbxByMotionContaining(@Param("message") String message, Pageable pageable);
}
