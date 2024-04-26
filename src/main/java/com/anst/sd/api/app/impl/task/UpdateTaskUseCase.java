package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.app.api.task.UpdateTaskInBound;
import com.anst.sd.api.domain.notification.PendingNotification;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateTaskUseCase implements UpdateTaskInBound {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final DateConverterDelegate dateConverterDelegate;

    @Override
    @Transactional
    public Task update(Long userId, Long taskId, Task updated) {
        log.info("Updating task with id {} and userId {}", taskId, userId);
        Task task = taskRepository.findByIdAndUser(taskId, userId);
        mergeTask(task, updated, userId);
        return taskRepository.save(task);
    }

    private void mergeTask(Task original, Task updated, Long userId) {
        List<PendingNotification> convertedNotifications = new ArrayList<>();
        original.setData(updated.getData());
        original.setDeadline(updated.getDeadline());
        original.setDescription(updated.getDescription());
        original.setStatus(updated.getStatus());
        if (updated.getPendingNotifications() != null) {
            convertedNotifications = updated.getPendingNotifications().stream()
                    .map(notification -> dateConverterDelegate.convertToInstant(updated.getDeadline(), notification))
                    .collect(Collectors.toList());
        }
        original.setPendingNotifications(convertedNotifications);
        if (updated.getUpdatedProjectId() != null &&
            !updated.getUpdatedProjectId().equals(original.getProject().getId())) {
            Project newProject = projectRepository.getByIdAndUserId(updated.getUpdatedProjectId(), userId);
            original.setProject(newProject);
        }
    }
}
