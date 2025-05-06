package com.anst.sd.api.app.impl.sprint;

import com.anst.sd.api.app.api.sprint.AttachTaskToSprintInBound;
import com.anst.sd.api.app.api.sprint.SprintRepository;
import com.anst.sd.api.app.api.sprint.SprintValidationException;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.app.impl.notification.NotificationCreatedEvent;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.DefectTask;
import com.anst.sd.api.domain.task.StoryTask;
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
import static com.anst.sd.api.domain.notification.NotificationTemplate.TASK_MOVED_TO_NEW_SPRINT;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachTaskToSprintUseCase implements AttachTaskToSprintInBound {
    private final SprintRepository sprintRepository;
    private final AbstractTaskRepository taskRepository;
    private final SprintValidationDelegate sprintValidationDelegate;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public void attach(UUID userId, UUID sprintId, String simpleId) {
        Sprint sprint = sprintRepository.getById(sprintId);
        AbstractTask task = taskRepository.getBySimpleId(simpleId);
        sprintValidationDelegate.validateSameProject(sprint, task);
        sprintValidationDelegate.validateUserHasAccessToProject(userId, sprint.getProject().getId());
        sprintValidationDelegate.validateAndAttach(sprint, task);
        taskRepository.save(task);
        sendNotifications(task, userId);
    }

    private void sendNotifications(AbstractTask task, UUID userId) {
        Collection<User> recipients = Stream.of(task.getAssignee(), task.getCreator(), task.getReviewer())
                .filter(Objects::nonNull)
                .filter(user -> !user.getId().equals(userId))
                .collect(Collectors.toMap(User::getId, user -> user, (a, b) -> a))
                .values();
        Sprint sprint;
        if (task instanceof DefectTask defect) {
            sprint = defect.getSprint();
        } else if (task instanceof StoryTask story) {
            sprint = story.getSprint();
        } else {
            throw new SprintValidationException("Undefined task type while attaching to sprint");
        }
        User changer = userRepository.getById(userId);
        recipients.forEach(recipient -> {
            NotificationCreatedEvent event = new NotificationCreatedEvent();
            event.setTemplate(TASK_MOVED_TO_NEW_SPRINT);
            event.setRecipient(recipient);
            event.setParams(Map.of(
                    TASK_SIMPLE_ID.getKey(), task.getSimpleId(),
                    TASK_TITLE.getKey(), task.getName(),
                    SPRINT_NAME.getKey(), sprint.getName(),
                    USER_NAME.getKey(), "%s %s".formatted(changer.getFirstName(), changer.getLastName())
            ));
            applicationEventPublisher.publishEvent(event);
        });
    }
}
