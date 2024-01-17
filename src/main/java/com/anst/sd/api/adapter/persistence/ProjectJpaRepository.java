package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdAndUserId(Long id, Long userId);
}
