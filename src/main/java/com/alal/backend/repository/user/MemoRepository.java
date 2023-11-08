package com.alal.backend.repository.user;

import com.alal.backend.domain.entity.user.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long>{
}
