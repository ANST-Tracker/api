package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.*;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.app.impl.notification.NotificationCreatedEvent;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.SimpleDictionary;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.*;
import static com.anst.sd.api.domain.notification.NotificationTemplate.TASK_STATUS_UPDATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateAbstractTaskStatusUseCase implements UpdateAbstractTaskStatusInBound {
    private final AbstractTaskRepository abstractTaskRepository;
    private final GetAvailableStatusesInBound getAvailableStatusesInBound;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AbstractTask updateStatus(UUID userId, String simpleId, String status) {
        log.info("Updating task status for taskId {} to new status: {}", simpleId, status);
        AbstractTask task = abstractTaskRepository.getBySimpleId(simpleId);
        TaskStatus newStatus;
        try {
            newStatus = TaskStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new AbstractTaskValidationException("Invalid status: " + status);
        }

        List<SimpleDictionary> availableStatuses = getAvailableStatusesInBound.getAppropriateStatuses(simpleId);
        boolean isValidTransition = availableStatuses.stream()
                .anyMatch(sd -> sd.getCode().equals(status));
        if (!isValidTransition) {
            throw new AbstractTaskAppropriateStatusException(status, task.getStatus().toString());
        }
        TaskStatus oldStatus = task.getStatus();
        task.setStatus(newStatus);
        createNotifications(task, oldStatus, userRepository.getById(userId));
        return abstractTaskRepository.save(task);
    }

    private void createNotifications(AbstractTask abstractTask, TaskStatus oldStatus, User author) {
        Collection<User> recipients = Stream.of(
                        abstractTask.getAssignee(),
                        abstractTask.getCreator(),
                        abstractTask.getReviewer()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(User::getId, user -> user, (a, b) -> a))
                .values();
        recipients.forEach(recipient -> {
            NotificationCreatedEvent event = new NotificationCreatedEvent();
            event.setTemplate(TASK_STATUS_UPDATED);
            event.setRecipient(recipient);
            event.setParams(Map.of(
                    USER_NAME.getKey(), "%s %s".formatted(author.getFirstName(), author.getLastName()),
                    TASK_SIMPLE_ID.getKey(), abstractTask.getSimpleId(),
                    TASK_TITLE.getKey(), abstractTask.getName(),
                    OLD_TASK_STATUS.getKey(), oldStatus.getValue(),
                    NEW_TASK_STATUS.getKey(), abstractTask.getStatus().getValue()
            ));
            applicationEventPublisher.publishEvent(event);
        });
    }
}