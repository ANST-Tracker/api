package com.anst.sd.api.app.impl.task.comment;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.comment.CommentRepository;
import com.anst.sd.api.app.api.task.comment.CreateCommentInbound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.app.impl.notification.NotificationCreatedEvent;
import com.anst.sd.api.domain.task.Comment;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.*;
import static com.anst.sd.api.domain.notification.NotificationTemplate.USER_CREATED_COMMENT;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateCommentUseCase implements CreateCommentInbound {
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AbstractTaskRepository abstractTaskRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

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
        sendNotification(comment);
        return commentRepository.save(comment);
    }

    private void sendNotification(Comment comment) {
        Collection<User> usersForNotification = Stream.of(
                        comment.getTask().getAssignee(),
                        comment.getTask().getCreator(),
                        comment.getTask().getReviewer())
                .filter(Objects::nonNull)
                .map(user -> userRepository.getById(user.getId()))
                .filter(user -> !Objects.equals(comment.getAuthor().getId(), user.getId()))
                .collect(Collectors.toMap(User::getId, user -> user, (a, b) -> a))
                .values();
        usersForNotification.forEach(user -> {
            NotificationCreatedEvent event = createNotification(comment, user);
            applicationEventPublisher.publishEvent(event);
        });
    }

    private NotificationCreatedEvent createNotification(Comment comment, User user) {
        NotificationCreatedEvent event = new NotificationCreatedEvent();
        event.setRecipient(user);
        event.setTemplate(USER_CREATED_COMMENT);
        event.setParams(Map.of(
                TASK_SIMPLE_ID.getKey(), comment.getTask().getSimpleId(),
                TASK_TITLE.getKey(), comment.getTask().getName(),
                COMMENT_DATA.getKey(), comment.getContent(),
                USER_NAME.getKey(), "%s %s".formatted(comment.getAuthor().getFirstName(), comment.getAuthor().getLastName())
        ));
        return event;
    }
}
