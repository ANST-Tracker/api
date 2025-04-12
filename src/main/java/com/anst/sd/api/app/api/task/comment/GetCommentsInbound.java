package com.anst.sd.api.app.api.task.comment;

import com.anst.sd.api.domain.task.Comment;

import java.util.List;
import java.util.UUID;

public interface GetCommentsInbound {
    List<Comment> get(UUID userId, UUID projectId, String taskId);
}
