package com.anst.sd.api.app.impl.task.comment;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.comment.CommentRepository;
import com.anst.sd.api.app.api.task.comment.UpdateCommentInbound;
import com.anst.sd.api.domain.task.Comment;
import com.anst.sd.api.security.app.api.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateCommentUseCase implements UpdateCommentInbound {
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment update(UUID id, String content, UUID projectId, String taskId, UUID authorId) {
        log.info("Update comment {} in project {} for task {} by user {} with content {}",
                id, projectId, taskId, authorId, content);
        if (!projectRepository.existsByIdAndUserId(projectId, authorId)) {
            throw new AuthException("No auth for user %s in project %s".formatted(authorId, projectId));
        }
        Comment comment = commentRepository.findByIdAndTaskAndProjectIdAndAuthor(
                id, taskId, projectId, authorId);
        comment.setContent(content);
        return commentRepository.save(comment);
    }
}
