package com.anst.sd.api.app.api.project;

import java.util.UUID;

public class ProjectNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE_BY_USER = "There isn't project with id %s";

    public ProjectNotFoundException(UUID id) {
        super(ERROR_MESSAGE_BY_USER.formatted(id));
    }
}
