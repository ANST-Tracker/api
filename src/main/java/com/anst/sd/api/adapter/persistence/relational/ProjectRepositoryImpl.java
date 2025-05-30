package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.project.ProjectNotFoundException;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.domain.project.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {
    private final ProjectJpaRepository projectJpaRepository;

    @Override
    public Project save(Project project) {
        return projectJpaRepository.save(project);
    }

    @Override
    public Project getById(UUID id) {
        return projectJpaRepository.findById(id)
            .orElseThrow(() -> new ProjectNotFoundException(id));
    }

    @Override
    public Project getByIdAndUserId(UUID projectId, UUID userId) {
        Project project = projectJpaRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId, userId));

        if (project == null) {
            throw new ProjectNotFoundException(projectId, userId);
        }

        return project;
    }

    @Override
    public List<Project> getAllByUserId(UUID userId) {
        return projectJpaRepository.findAllByUserId(userId);
    }

    @Override
    public boolean existsByIdAndUserId(UUID id, UUID userId) {
        return projectJpaRepository.existsByIdAndUserId(id, userId, userId);
    }
}
