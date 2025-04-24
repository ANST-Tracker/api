package com.anst.sd.api.app.api.task.log;

import java.util.UUID;

public class LogNotFound extends RuntimeException {
    private static final String ERROR_MESSAGE = "Not found log by id %s, task %s in project %s from user %s";

    public LogNotFound(UUID id, String taskId, UUID projectId, UUID authorId) {
        super(ERROR_MESSAGE.formatted(id, taskId, projectId, authorId));
    }
}