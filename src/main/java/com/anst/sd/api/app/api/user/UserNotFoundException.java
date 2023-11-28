package com.anst.sd.api.app.api.user;

public class UserNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User with id %s was not found";

    public UserNotFoundException(Long userId) {
        super(ERROR_MESSAGE.formatted(userId));
    }
}
