package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findById(UUID id);
}
