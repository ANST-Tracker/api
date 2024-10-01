package com.anst.sd.api.app.impl.project;

import com.anst.sd.api.app.api.project.ProjectNotFoundException;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.project.UpdateProjectInbound;
import com.anst.sd.api.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.anst.sd.api.domain.project.ProjectType.BUCKET;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateProjectUseCase implements UpdateProjectInbound {
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Project update(Long id, Project updated, Long userId) {
        log.info("Updating project with id {} and userId {}", id, userId);
        Project original = projectRepository.getByIdAndUserId(id, userId);
        if (BUCKET.equals(original.getProjectType())) {
            throw new ProjectNotFoundException(id, userId);
        }
        original.setName(updated.getName());
        return projectRepository.save(original);
    }
}
