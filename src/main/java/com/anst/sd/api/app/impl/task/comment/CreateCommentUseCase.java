package com.anst.sd.api.app.impl.task.comment;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.comment.CommentRepository;
import com.anst.sd.api.app.api.task.comment.CreateCommentInbound;
import com.anst.sd.api.app.api.user.UserRepository;
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
public class CreateCommentUseCase implements CreateCommentInbound {
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AbstractTaskRepository abstractTaskRepository;

    @Override
    @Transactional
    public Comment create(String content, UUID projectId, String taskId, UUID authorId) {
        log.info("Creating comment in project {} for task {} by user {} with content {}",
                projectId, taskId, authorId, content);
        if (!projectRepository.existsByIdAndUserId(projectId, authorId)) {
            throw new AuthException("No auth for user %s in project %s".formatted(authorId, projectId));
        }
        Comment comment = new Comment();
        comment
                .setContent(content)
                .setTask(abstractTaskRepository.getByIdAndProjectId(taskId, projectId))
                .setAuthor(userRepository.getById(authorId));
        return commentRepository.save(comment);
    }
}
