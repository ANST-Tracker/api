package com.anst.sd.api.app.api.project;

import java.util.UUID;

public class ProjectValidationException extends RuntimeException {
    private static final String ERROR_UPDATE = "Invalid update request for projectId %s";
    private static final String ERROR_CREATE = "Invalid create project request";
    private static final String ERROR_HAS_NO_ACCESS = "UserId %s has no access for the projectId %s";

    public ProjectValidationException(UUID projectId) {
        super(ERROR_UPDATE.formatted(projectId));
    }

    public ProjectValidationException() {
        super(ERROR_CREATE);
    }

    public ProjectValidationException(UUID userId, UUID projectId) {
        super(ERROR_HAS_NO_ACCESS.formatted(userId, projectId));
    }
}
