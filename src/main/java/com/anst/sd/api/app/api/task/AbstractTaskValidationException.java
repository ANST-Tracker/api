package com.anst.sd.api.app.api.task;

public class AbstractTaskValidationException extends RuntimeException {
    private static final String ERROR_UPDATE = "Invalid update request for taskId %s";
    private static final String ERROR_CREATE = "Invalid create task request";

    public AbstractTaskValidationException() {
        super(ERROR_CREATE);
    }

    public AbstractTaskValidationException(String taskId) {
        super(ERROR_UPDATE.formatted(taskId));
    }
}
