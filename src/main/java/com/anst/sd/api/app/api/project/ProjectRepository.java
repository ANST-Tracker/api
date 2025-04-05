package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

import java.util.UUID;

public interface ProjectRepository {
    Project save(Project project);

    Project getById(UUID id);

    boolean existsByIdAndUserId(UUID userId, UUID id);

    Project getByIdAndUserId(UUID projectId, UUID userId);
}
