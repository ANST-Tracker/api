package com.anst.sd.api.app.api.security;

public class RoleNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Role %s was not found";

    public RoleNotFoundException(String role) {
        super(ERROR_MESSAGE.formatted(role));
    }
}
