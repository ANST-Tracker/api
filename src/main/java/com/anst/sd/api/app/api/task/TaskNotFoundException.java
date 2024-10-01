package com.anst.sd.api.app.api.task;

public class TaskNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Task with id %s not found for user with id %s";

    public TaskNotFoundException(Long taskId, Long userId) {
        super(ERROR_MESSAGE.formatted(taskId, userId));
    }
}
