package com.anst.sd.api.app.impl.task.comment;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.comment.CommentRepository;
import com.anst.sd.api.app.api.task.comment.GetCommentsInbound;
import com.anst.sd.api.domain.task.Comment;
import com.anst.sd.api.security.app.api.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetCommentsUseCase implements GetCommentsInbound {
    private final CommentRepository commentRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Comment> get(UUID userId, UUID projectId, String taskId) {
        log.info("User {} in project {} getting comments for task {}", userId, projectId, taskId);
        if (!projectRepository.existsByIdAndUserId(projectId, userId)) {
            throw new AuthException("No auth for user %s in project %s".formatted(userId, projectId));
        }
        return commentRepository.findAll(taskId, projectId);
    }
}
