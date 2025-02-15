package com.anst.sd.api.app.impl.project;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.project.UpdateProjectNextTaskIdInbound;
import com.anst.sd.api.domain.project.Project;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateProjectNextTaskIdUseCase implements UpdateProjectNextTaskIdInbound {
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Project updateNextTaskId(UUID id) {
        log.info("Update next task id for project with id {}", id);
        Project project = projectRepository.getById(id);
        project.setNextTaskId(project.getNextTaskId() + 1);
        return projectRepository.save(project);
    }
}
