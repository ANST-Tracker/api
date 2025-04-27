package com.anst.sd.api.app.impl.task.log;

import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.log.LogRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.app.impl.notification.NotificationCreatedEvent;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.*;
import static com.anst.sd.api.domain.notification.NotificationTemplate.TASK_TIME_LOG_UPDATED;

@Component
@RequiredArgsConstructor
public class TotalLogNotificationGenerator {
    private final AbstractTaskRepository abstractTaskRepository;
    private final LogRepository logRepository;
    private final UserRepository userRepository;

    public List<NotificationCreatedEvent> create(String taskId, UUID userId) {
        AbstractTask task = abstractTaskRepository.getBySimpleId(taskId);
        Collection<User> recipients = Stream.of(
                        task.getAssignee(),
                        task.getReviewer(),
                        task.getCreator())
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(User::getId, user -> user, (a, b) -> a))
                .values();
        User changer = userRepository.getById(userId);
        return recipients.stream()
                .map(recipient -> {
                    NotificationCreatedEvent event = new NotificationCreatedEvent();
                    event.setRecipient(recipient);
                    event.setTemplate(TASK_TIME_LOG_UPDATED);
                    event.setParams(Map.of(
                            TASK_SIMPLE_ID.getKey(), task.getSimpleId(),
                            TASK_TITLE.getKey(), task.getName(),
                            USER_NAME.getKey(), "%s %s".formatted(changer.getFirstName(), changer.getLastName()),
                            USED_TIME.getKey(), calculateTotalLogs(task) + "h"
                    ));
                    return event;
                })
                .toList();
    }

    private BigDecimal calculateTotalLogs(AbstractTask original) {
        List<BigDecimal> logsHours = logRepository.findAll(original.getSimpleId(), original.getProject().getId()).stream()
                .map(log -> {
                    Duration duration = Duration.of(
                            log.getTimeEstimation().getAmount(),
                            log.getTimeEstimation().getTimeUnit().toChronoUnit());
                    return BigDecimal.valueOf((double) duration.getSeconds() / 3600.0);
                })
                .toList();
        return logsHours.stream().reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2);
    }
}
