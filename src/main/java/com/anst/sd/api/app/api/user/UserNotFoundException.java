package com.anst.sd.api.app.api.user;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User with id %s was not found";
    private static final String ERROR_MESSAGE_USERNAME = "User with name %s was not found";

    public UserNotFoundException(UUID userId) {
        super(ERROR_MESSAGE.formatted(userId));
    }

    public UserNotFoundException(String username) {
        super(ERROR_MESSAGE_USERNAME.formatted(username));
    }
}