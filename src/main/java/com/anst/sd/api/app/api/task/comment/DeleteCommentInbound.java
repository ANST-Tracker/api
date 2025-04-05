package com.anst.sd.api.app.api.task.comment;

import com.anst.sd.api.domain.task.Comment;

import java.util.UUID;

public interface DeleteCommentInbound {
    Comment delete(UUID id, UUID projectId, String taskId, UUID authorId);
}
