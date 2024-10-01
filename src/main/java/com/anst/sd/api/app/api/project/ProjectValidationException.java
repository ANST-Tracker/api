package com.anst.sd.api.app.api.project;

public class ProjectValidationException extends RuntimeException {
    private static final String ERROR_UPDATE = "Invalid update request for projectId %d";
    private static final String ERROR_CREATE = "Invalid create project request";

    public ProjectValidationException(Long projectId) {
        super(ERROR_UPDATE.formatted(projectId));
    }

    public ProjectValidationException() {
        super(ERROR_CREATE);
    }
}
