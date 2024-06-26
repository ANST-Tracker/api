package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.project.ProjectNotFoundException;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.domain.project.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.anst.sd.api.domain.project.ProjectType.BUCKET;

@Component
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {
    private final ProjectJpaRepository projectJpaRepository;

    @Override
    public Project save(Project project) {
        return projectJpaRepository.save(project);
    }

    @Override
    public Project getByIdAndUserId(Long id, Long userId) {
        return projectJpaRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new ProjectNotFoundException(id, userId));
    }

    @Override
    public Project getBucketProject(String telegramUserId) {
        return projectJpaRepository.findFirstByProjectTypeAndUserTelegramId(BUCKET, telegramUserId)
            .orElseThrow(() -> new ProjectNotFoundException(BUCKET, telegramUserId));
    }

    @Override
    public List<Project> findByUserId(Long userId) {
        return projectJpaRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        projectJpaRepository.deleteById(id);
    }
}
