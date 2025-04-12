package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.task.comment.CommentNotFound;
import com.anst.sd.api.app.api.task.comment.CommentRepository;
import com.anst.sd.api.domain.task.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public List<Comment> findAll(String taskId, UUID projectId) {
        return commentJpaRepository.findAllByTaskAndProject(taskId, projectId);
    }

    @Override
    public Comment findByIdAndTaskAndProjectIdAndAuthor(UUID id, String taskId, UUID projectId, UUID authorId) {
        return commentJpaRepository.findByIdAndTaskAndProjectAndAuthor(id, taskId, projectId, authorId)
                .orElseThrow(() -> new CommentNotFound(id, taskId, projectId, authorId));
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(comment);
    }
}
