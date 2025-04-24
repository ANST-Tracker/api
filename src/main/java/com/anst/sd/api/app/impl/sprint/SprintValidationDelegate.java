package com.anst.sd.api.app.impl.sprint;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.project.ProjectValidationException;
import com.anst.sd.api.app.api.sprint.SprintValidationException;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.DefectTask;
import com.anst.sd.api.domain.task.StoryTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SprintValidationDelegate {
    private final ProjectRepository projectRepository;

    public void validateAndAttach(Sprint sprint, AbstractTask task) {
        if (task instanceof StoryTask story) {
            story.setSprint(sprint);
        } else if (task instanceof DefectTask defect) {
            defect.setSprint(sprint);
        } else {
            throw new SprintValidationException("Only story or defect can be attached");
        }
    }

    public void validateSameProject(Sprint sprint, AbstractTask task) {
        UUID sprintProjectId = sprint.getProject().getId();
        UUID taskProjectId = task.getProject().getId();

        if (!sprintProjectId.equals(taskProjectId)) {
            throw new SprintValidationException("Task and sprint are not in the same project");
        }
    }

    public void validateUserHasAccessToProject(UUID userId, UUID projectId) {
        if (!projectRepository.existsByIdAndUserId(projectId, userId)) {
            throw new ProjectValidationException(userId, projectId);
        }
    }
}
