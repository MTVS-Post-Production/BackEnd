package com.alal.backend.repository.group;

import com.alal.backend.domain.entity.project.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Avatar findByAvatarId(Long avatarId);
}
