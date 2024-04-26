package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.CreateTaskInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.notification.PendingNotification;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskUseCase implements CreateTaskInBound {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final DateConverterDelegate dateConverterDelegate;

    @Override
    @Transactional
    public Task create(Long userId, Long projectId, Task task) {
        log.info("Creating task with userId {} in project {}", userId, projectId);
        Project project = projectRepository.getByIdAndUserId(projectId, userId);
        task.setProject(project);
        task.setStatus(TaskStatus.BACKLOG);
        if (task.getPendingNotifications() != null) {
            List<PendingNotification> convertedNotifications = task.getPendingNotifications().stream()
                    .map(notification -> dateConverterDelegate.convertToInstant(task.getDeadline(), notification))
                    .collect(Collectors.toList());
            task.setPendingNotifications(convertedNotifications);
        }
        return taskRepository.save(task);
    }
}