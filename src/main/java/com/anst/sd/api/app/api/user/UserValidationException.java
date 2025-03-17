package com.anst.sd.api.app.api.user;

public class UserValidationException extends RuntimeException{
    private static final String ERROR_CREATE = "Invalid create user request";

    public UserValidationException() {
        super(ERROR_CREATE);
    }
}
