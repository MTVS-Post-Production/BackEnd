package com.alal.backend.repository.user;

import com.alal.backend.domain.entity.user.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByUserId(Long id);

    Page<Image> findByUserId(Long userId, Pageable pageable);
}
