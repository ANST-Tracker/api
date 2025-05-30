package com.anst.sd.api.app.api.project;

import java.util.UUID;

public class ProjectNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE_BY_USER = "There is not project with id %s";
    private static final String ERROR_MESSAGE_BY_USER_AND_ID = "There is not project with id %s for user with id %s";

    public ProjectNotFoundException(UUID id) {
        super(ERROR_MESSAGE_BY_USER.formatted(id));
    }

    public ProjectNotFoundException(UUID id, UUID userId) {
        super(String.format(ERROR_MESSAGE_BY_USER_AND_ID, id, userId));
    }
}
