package com.anst.sd.api.app.api.users_projects;

public class UsersProjectsValidationException extends RuntimeException {
    private static final String ERROR_CREATE = "Invalid add user in project";

    public UsersProjectsValidationException() {
        super(ERROR_CREATE);
    }
}
