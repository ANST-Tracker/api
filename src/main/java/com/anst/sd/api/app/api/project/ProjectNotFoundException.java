package com.anst.sd.api.app.api.project;

public class ProjectNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE_BY_USER = "User with id %d doesn't have project with id %d";

    public ProjectNotFoundException(Long id, Long userId) {
        super(ERROR_MESSAGE_BY_USER.formatted(userId, id));
    }
}
