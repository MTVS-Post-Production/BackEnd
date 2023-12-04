package com.alal.backend.repository.group;

import com.alal.backend.domain.entity.project.Project;
import com.alal.backend.domain.entity.project.ProjectStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectStaff, Long> {
    List<ProjectStaff> findAllByProject(Project project);
}
