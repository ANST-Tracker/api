package com.anst.sd.api.app.impl.project;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.project.UpdateProjectInbound;
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
public class UpdateProjectUseCase implements UpdateProjectInbound {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Project update(UUID id, Project updated, UUID userId) {
        log.info("Updating project with id {}, and userId {}", id, userId);
        Project original = projectRepository.getById(id);
        original.setName(updated.getName());
        original.setHead(userRepository.getById(updated.getHead().getId()));
        original.setDescription(updated.getDescription());
        return projectRepository.save(original);
    }
}
