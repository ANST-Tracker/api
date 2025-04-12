package com.anst.sd.api.app.api.task.comment;

import java.util.UUID;

public class CommentNotFound extends RuntimeException {
    private static final String ERROR_MESSAGE = "No comment by id %s, task %s in project %s from author %s";

    public CommentNotFound(UUID id, String taskId, UUID projectId, UUID authorId) {
        super(ERROR_MESSAGE.formatted(id, taskId, projectId, authorId));
    }
}