package com.anst.sd.api.app.impl.project;

import com.anst.sd.api.app.api.project.CreateProjectInbound;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CreateProjectUseCase implements CreateProjectInbound {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Project create(Project project, UUID userId) {
        log.info("Creating project named {}, with head ID {}, created by user with ID {}",
                project.getName(),
                project.getHead().getId(),
                userId);
        project.setHead(userRepository.getById(project.getHead().getId()));
        project.setNextTaskId(1);
        return projectRepository.save(project);
    }
}
