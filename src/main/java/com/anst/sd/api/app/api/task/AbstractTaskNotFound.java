package com.anst.sd.api.app.api.task;

import java.util.UUID;

public class AbstractTaskNotFound extends RuntimeException {
    private static final String ERROR_MESSAGE_TOKEN_SIMPLE_ID = "Abstract task was not found by simpleId %s";
    private static final String ERROR_MESSAGE_TOKEN_TASK_ID = "Abstract task was not found by taskId %s";

    public AbstractTaskNotFound(String simpleId) {
        super(ERROR_MESSAGE_TOKEN_SIMPLE_ID.formatted(simpleId));
    }

    public AbstractTaskNotFound(UUID taskId) {
        super(ERROR_MESSAGE_TOKEN_TASK_ID.formatted(taskId));
    }
}