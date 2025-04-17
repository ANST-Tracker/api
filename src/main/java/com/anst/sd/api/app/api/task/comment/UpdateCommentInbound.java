package com.anst.sd.api.app.api.task.comment;

import com.anst.sd.api.domain.task.Comment;

import java.util.UUID;

public interface UpdateCommentInbound {
    Comment update(UUID id, String content, UUID projectId, String taskId, UUID authorId);
}
