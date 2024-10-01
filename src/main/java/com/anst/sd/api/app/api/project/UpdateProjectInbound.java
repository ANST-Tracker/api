package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

public interface UpdateProjectInbound {
    Project update(Long id, Project project, Long userId);
}
