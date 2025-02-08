package com.anst.sd.api.app.api.task;

import java.util.UUID;

public class AbstractTaskNotFound extends RuntimeException {
    private static final String ERROR_MESSAGE_TOKEN = "Abstract task was not found by id %s";

    public AbstractTaskNotFound(UUID taskId) {
        super(ERROR_MESSAGE_TOKEN.formatted(taskId));
    }
}
