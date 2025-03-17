package com.anst.sd.api.app.api.task;

public class AbstractTaskAppropriateStatusException extends RuntimeException {
    public static final String INVALID_STATUS_EXCEPTION_MESSAGE = "Cannot transition to status %s from current status %s";

    public AbstractTaskAppropriateStatusException(String targetStatus, String currentStatus) {
        super(INVALID_STATUS_EXCEPTION_MESSAGE.formatted(targetStatus, currentStatus));
    }
}
