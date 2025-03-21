package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.UsersProjects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface UsersProjectsJpaRepository extends JpaRepository<UsersProjects, UUID> {
    List<UsersProjects> findByUserId(UUID userId);

    List<UsersProjects> findByProjectId(UUID projectId);

    Optional<UsersProjects> findByUserIdAndProjectId(UUID userId, UUID projectId);

    boolean existsByUserIdAndProjectId(UUID userId, UUID projectId);
}
