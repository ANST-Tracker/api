package com.anst.sd.api.app.api.task.comment;

public class CommentValidationException extends RuntimeException {
    private static final String ERROR_UPDATE = "Invalid update request for comment %s";
    private static final String ERROR_CREATE = "Invalid create comment request";

    public CommentValidationException() {
        super(ERROR_CREATE);
    }

    public CommentValidationException(String taskId) {
        super(ERROR_UPDATE.formatted(taskId));
    }
}
