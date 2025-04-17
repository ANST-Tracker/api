package com.anst.sd.api.app.impl.sprint;

import com.anst.sd.api.app.api.sprint.AttachTaskToSprintInBound;
import com.anst.sd.api.app.api.sprint.SprintRepository;
import com.anst.sd.api.app.api.sprint.SprintValidationException;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.DefectTask;
import com.anst.sd.api.domain.task.StoryTask;
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

    @Override
    @Transactional
    public void attach(UUID userId, UUID sprintId, String simpleId) {
        Sprint sprint = sprintRepository.getById(sprintId);
        AbstractTask task = taskRepository.getBySimpleId(simpleId);
        validateAndAttach(sprint, task);
        taskRepository.save(task);
    }

    private void validateAndAttach(Sprint sprint, AbstractTask task) {
        if (task instanceof StoryTask story) {
            story.setSprint(sprint);
        } else if (task instanceof DefectTask defect) {
            defect.setSprint(sprint);
        } else {
            throw new SprintValidationException("Only story or defect can be attached");
        }
    }
}
