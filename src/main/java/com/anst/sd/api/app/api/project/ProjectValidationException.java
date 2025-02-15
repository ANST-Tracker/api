package com.anst.sd.api.app.api.project;

import java.util.UUID;

public class ProjectValidationException extends RuntimeException {
    private static final String ERROR_UPDATE = "Invalid update request for projectId %s";
    private static final String ERROR_CREATE = "Invalid create project request";

    public ProjectValidationException(UUID projectId) {
        super(ERROR_UPDATE.formatted(projectId));
    }

    public ProjectValidationException() {
        super(ERROR_CREATE);
    }
}
