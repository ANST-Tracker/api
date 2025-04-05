package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.UsersProjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersProjectsJpaRepository extends JpaRepository<UsersProjects, UUID> {
    List<UsersProjects> findByProjectId(UUID projectId);

    Optional<UsersProjects> findByUserIdAndProjectId(UUID userId, UUID projectId);

    boolean existsByUserIdAndProjectId(UUID userId, UUID projectId);
}
