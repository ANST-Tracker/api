package com.anst.sd.api.app.api.usersProjects;

import java.util.UUID;

public class UsersProjectsNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User with id %s not found in project with id %s";

    public UsersProjectsNotFoundException(UUID userId, UUID projectId) {
        super(ERROR_MESSAGE.formatted(userId, projectId));
    }
}
