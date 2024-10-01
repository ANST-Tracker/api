package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.task.CreateTaskInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.notification.PendingNotification;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskUseCase implements CreateTaskInBound {
  private final TaskRepository taskRepository;
  private final ProjectRepository projectRepository;
  private final DateConverterDelegate dateConverterDelegate;
  private final TagRepository tagRepository;

  @Override
  @Transactional
  public Task create(Long userId, Long projectId, Task task) {
    log.info("Creating task with userId {} in project {}", userId, projectId);
    Project project = projectRepository.getByIdAndUserId(projectId, userId);
    task.setProject(project)
            .setStatus(TaskStatus.BACKLOG);
    if (task.getTags() != null && !task.getTags().isEmpty()) {
      List<Long> tags = task.getTags().stream()
              .map(Tag::getId)
              .toList();
      List<Tag> fullTags = tagRepository.findAllByIds(tags);
      task.setTags(fullTags);
    }
    if (task.getPendingNotifications() != null && task.getDeadline() != null) {
      List<PendingNotification> convertedNotifications = task.getPendingNotifications().stream()
              .map(notification -> dateConverterDelegate.convertToInstant(task.getDeadline(), notification))
              .toList();
      task.setPendingNotifications(convertedNotifications);
    }
    return taskRepository.save(task);
  }

  @Override
  @Transactional
  public void create(String userTelegramId, String name) {
    log.info("Internal: creating task for telegram user {} with name {}", userTelegramId, name);
    Project bucketProject = projectRepository.getBucketProject(userTelegramId);
    Task task = new Task()
            .setData(name)
            .setProject(bucketProject)
            .setStatus(TaskStatus.BACKLOG);
    taskRepository.save(task);
  }
}