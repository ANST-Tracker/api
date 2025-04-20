package com.anst.sd.api.app.impl.sprint;

import com.anst.sd.api.app.api.sprint.AttachTaskToSprintInBound;
import com.anst.sd.api.app.api.sprint.SprintRepository;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.task.AbstractTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachTaskToSprintUseCase implements AttachTaskToSprintInBound {
    private final SprintRepository sprintRepository;
    private final AbstractTaskRepository taskRepository;
    private final SprintValidationDelegate sprintValidationDelegate;

    @Override
    @Transactional
    public void attach(UUID userId, UUID sprintId, String simpleId) {
        Sprint sprint = sprintRepository.getById(sprintId);
        AbstractTask task = taskRepository.getBySimpleId(simpleId);
        sprintValidationDelegate.validateSameProject(sprint, task);
        sprintValidationDelegate.validateUserHasAccessToProject(userId, sprint.getProject().getId());
        sprintValidationDelegate.validateAndAttach(sprint, task);
        taskRepository.save(task);
    }
}
