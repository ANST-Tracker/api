package com.anst.sd.api.app.api.usersProjects;

import com.anst.sd.api.domain.UsersProjects;

import java.util.UUID;

public interface UsersProjectsRepository {
    UsersProjects findByUserIdAndProjectId(UUID userId, UUID projectId);

    UsersProjects save(UsersProjects usersProjects);

    void delete(UUID id);

    boolean existsByUserIdAndProjectId(UUID userId, UUID projectId);
}
