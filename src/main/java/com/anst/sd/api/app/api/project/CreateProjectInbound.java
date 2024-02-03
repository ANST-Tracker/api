package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

public interface CreateProjectInbound {
    Project create(Project project, Long userId);
}
