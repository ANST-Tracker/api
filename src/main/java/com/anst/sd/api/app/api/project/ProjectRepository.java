package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

import java.util.List;

public interface ProjectRepository {
    List<Project> saveAll(List<Project> projectList);

    Project save(Project project);

    Project getByIdAndUserId(Long id, Long userId);
}
