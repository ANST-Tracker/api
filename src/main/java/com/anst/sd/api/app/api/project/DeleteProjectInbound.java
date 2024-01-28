package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

public interface DeleteProjectInbound {
    Project delete(Long id, Long userId);
}
