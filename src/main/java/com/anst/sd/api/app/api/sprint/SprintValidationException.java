package com.anst.sd.api.app.api.sprint;

public class SprintValidationException extends RuntimeException {
    private static final String ERROR_UPDATE = "Invalid update request for sprintId %s";
    private static final String ERROR_CREATE = "Invalid create sprint request";

    public SprintValidationException() {
        super(ERROR_CREATE);
    }

    public SprintValidationException(String message) {
        super(ERROR_UPDATE.formatted(message));
    }
}
