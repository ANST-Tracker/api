package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.CreateTaskInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskUseCase implements CreateTaskInBound {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Task create(Long userId, Long projectId, Task task) {
        log.info("Creating task with userId {} in project {}", userId, projectId);
        Project project = projectRepository.getByIdAndUserId(projectId, userId);
        task.setProject(project);
        task.setStatus(TaskStatus.BACKLOG);
        return taskRepository.save(task);
    }
}
