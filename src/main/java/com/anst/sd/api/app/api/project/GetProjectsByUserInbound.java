package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

import java.util.List;
import java.util.UUID;

public interface GetProjectsByUserInbound {
    List<Project> get(UUID userId);
}
