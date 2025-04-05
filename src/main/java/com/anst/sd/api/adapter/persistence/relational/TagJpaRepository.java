package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.tag.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagJpaRepository extends JpaRepository<Tag, UUID> {
    List<Tag> findAllByProjectId(UUID projectId);

    @EntityGraph(attributePaths = "tasks")
    Optional<Tag> findById(UUID id);

    void deleteById(UUID id);

    List<Tag> findAllByIdIn(List<UUID> ids);

    Boolean existsByNameAndProjectId(String name, UUID projectId);

    Optional<Tag> findByNameAndProjectId(String name, UUID projectId);

    boolean existsById(UUID id);

    List<Tag> findAllByIdInAndProjectId(List<UUID> ids, UUID projectId);
}
