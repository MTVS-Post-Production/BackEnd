package com.alal.backend.repository.user;


import com.alal.backend.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
