package com.anst.sd.api.app.api.usersProjects;

public class UsersProjectsValidationException extends RuntimeException {
    private static final String ERROR_CREATE = "Invalid add user in project";

    public UsersProjectsValidationException() {
        super(ERROR_CREATE);
    }
}
