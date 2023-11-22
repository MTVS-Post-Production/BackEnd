package com.alal.backend.repository;

import com.alal.backend.domain.entity.project.ProjectAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectAvatarRepository extends JpaRepository<ProjectAvatar, Long> {
}
