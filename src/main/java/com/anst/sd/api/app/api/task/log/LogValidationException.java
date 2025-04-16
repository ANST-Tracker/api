package com.anst.sd.api.app.api.task.log;

public class LogValidationException extends RuntimeException {
    private static final String ERROR_UPDATE = "Invalid update request for log %s";
    private static final String ERROR_CREATE = "Invalid create log request";

    public LogValidationException() {
        super(ERROR_CREATE);
    }

    public LogValidationException(String taskId) {
        super(ERROR_UPDATE.formatted(taskId));
    }
}
