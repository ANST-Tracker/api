package com.anst.sd.api.app.api.task;

public class TaskValidationException extends RuntimeException {
    private static final String ERROR_UPDATE = "Invalid update request for taskId %d";
    private static final String ERROR_CREATE = "Invalid create task request";

    public TaskValidationException(Long taskId) {
        super(ERROR_UPDATE.formatted(taskId));
    }

    public TaskValidationException() {
        super(ERROR_CREATE);
    }
}
