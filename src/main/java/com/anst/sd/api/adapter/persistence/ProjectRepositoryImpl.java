package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.app.api.project.ProjectNotFoundException;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.domain.project.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {
    private final ProjectJpaRepository projectJpaRepository;

    @Override
    public List<Project> saveAll(List<Project> projectList) {
        return projectJpaRepository.saveAll(projectList);
    }

    @Override
    public Project save(Project project) {
        return projectJpaRepository.save(project);
    }

    @Override
    public Project getByIdAndUserId(Long id, Long userId) {
        return projectJpaRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ProjectNotFoundException(id, userId));
    }
}
