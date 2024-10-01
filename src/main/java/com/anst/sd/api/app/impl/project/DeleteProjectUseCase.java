package com.anst.sd.api.app.impl.project;

import com.anst.sd.api.app.api.project.DeleteProjectInbound;
import com.anst.sd.api.app.api.project.ProjectNotFoundException;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.anst.sd.api.domain.project.ProjectType.BUCKET;

@Slf4j
@Service
@AllArgsConstructor
public class DeleteProjectUseCase implements DeleteProjectInbound {
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Project delete(Long id, Long userId) {
        log.info("Deleting project with id {} and userId {}", id, userId);
        Project project = projectRepository.getByIdAndUserId(id, userId);
        if (BUCKET.equals(project.getProjectType())) {
            throw new ProjectNotFoundException(id, userId);
        }
        projectRepository.deleteById(id);
        return project;
    }
}
