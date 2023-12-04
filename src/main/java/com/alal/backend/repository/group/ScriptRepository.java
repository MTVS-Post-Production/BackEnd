package com.alal.backend.repository.group;

import com.alal.backend.domain.entity.project.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
}
