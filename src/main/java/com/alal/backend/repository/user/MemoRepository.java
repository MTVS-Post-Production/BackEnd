package com.alal.backend.repository.user;

import com.alal.backend.domain.entity.user.Memo;
import com.alal.backend.domain.entity.user.vo.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MemoRepository extends JpaRepository<Memo, Long>{
    Page<Memo> findAllByGroup(Group group, Pageable pageable);
}
