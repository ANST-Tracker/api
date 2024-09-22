package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagJpaRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUserId(Long userId);
    Tag findByIdAndUserId(Long id, Long userId);
}
