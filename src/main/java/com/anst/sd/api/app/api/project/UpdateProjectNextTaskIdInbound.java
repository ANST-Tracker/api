package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

import java.util.UUID;

public interface UpdateProjectNextTaskIdInbound {
    Project updateNextTaskId(UUID id);
}
