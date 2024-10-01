package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

import java.util.List;

public interface ProjectRepository {
  Project save(Project project);

  Project getByIdAndUserId(Long id, Long userId);

  Project getBucketProject(String telegramUserId);

  List<Project> findByUserId(Long userId);

  void deleteById(Long id);
}
