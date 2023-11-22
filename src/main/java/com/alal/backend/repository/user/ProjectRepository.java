package com.alal.backend.repository.user;

import com.alal.backend.domain.entity.user.Project;
import com.alal.backend.domain.entity.user.vo.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByGroupOrderByProjectIdDesc(Group group, Pageable pageable);
}
