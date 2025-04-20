package com.anst.sd.api.app.impl.sprint;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.sprint.CreateSprintInBound;
import com.anst.sd.api.app.api.sprint.SprintRepository;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.sprint.Sprint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateSprintUseCase implements CreateSprintInBound {
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    private final SprintValidationDelegate sprintValidationDelegate;

    @Override
    @Transactional
    public Sprint create(UUID userId, Sprint sprint) {
        log.info("Creating sprint named {} and description {}", sprint.getName(), sprint.getDescription());
        sprintValidationDelegate.validateUserHasAccessToProject(userId, sprint.getProject().getId());
        Project project = projectRepository.getById(sprint.getProject().getId());
        sprint.setIsActive(true);
        sprint.setProject(project);
        return sprintRepository.save(sprint);
    }
}
