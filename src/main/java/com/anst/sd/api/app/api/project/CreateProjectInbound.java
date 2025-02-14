package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

import java.util.UUID;

public interface CreateProjectInbound {
    Project create(Project project, UUID userId);
}
