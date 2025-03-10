package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TagJpaRepository extends JpaRepository<Tag, UUID> {
    List<Tag> findAllByIdIn(List<UUID> ids);
}
