package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository {
    Project save(Project project);

    Project getById(UUID id);

    boolean existsByIdAndUserId(UUID id, UUID userId);

    Project getByIdAndUserId(UUID projectId, UUID userId);

    List<Project> getAllByUserId(UUID userId);
}
