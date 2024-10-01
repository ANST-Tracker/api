package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, Long> {
  Optional<Project> findByIdAndUserId(Long id, Long userId);

  List<Project> findAllByUserId(Long userId);

  Optional<Project> findFirstByProjectTypeAndUserTelegramId(ProjectType type, String telegramUserId);
}
