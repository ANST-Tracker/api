package com.anst.sd.api.app.api.task.comment;

import com.anst.sd.api.domain.task.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentRepository {
    Comment save(Comment comment);

    List<Comment> findAll(String taskId, UUID projectId);

    Comment findByIdAndTaskAndProjectIdAndAuthor(UUID id, String taskId, UUID projectId, UUID authorId);

    void delete(Comment comment);
}
