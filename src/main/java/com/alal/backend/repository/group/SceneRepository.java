package com.alal.backend.repository.group;

import com.alal.backend.domain.entity.project.Scene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SceneRepository extends JpaRepository<Scene, Long> {
}
