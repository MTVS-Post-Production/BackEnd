package com.alal.backend.repository.group;

import com.alal.backend.domain.entity.project.Project;
import com.alal.backend.domain.entity.project.ProjectAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectAvatarRepository extends JpaRepository<ProjectAvatar, Long> {
    List<ProjectAvatar> findAllByProject(Project project);
}
