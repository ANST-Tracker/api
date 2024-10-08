package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagJpaRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUserId(Long userId);

    Optional<Tag> findByIdAndUserId(Long id, Long userId);

    Boolean existsByNameAndUserId(String name, Long userId);
}
