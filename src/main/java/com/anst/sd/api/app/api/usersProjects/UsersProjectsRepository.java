package com.anst.sd.api.app.api.usersProjects;

import com.anst.sd.api.domain.UsersProjects;

import java.util.List;
import java.util.UUID;

public interface UsersProjectsRepository {
    UsersProjects findByUserIdAndProjectId(UUID userId, UUID projectId);

    List<UsersProjects> findByProjectId(UUID projectId);

    UsersProjects save(UsersProjects usersProjects);

    void delete(UUID id);
}
