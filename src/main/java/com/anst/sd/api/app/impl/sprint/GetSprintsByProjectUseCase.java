package com.anst.sd.api.app.impl.sprint;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.sprint.GetSprintsByProjectInbound;
import com.anst.sd.api.app.api.sprint.SprintRepository;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.security.app.api.AuthException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class GetSprintsByProjectUseCase implements GetSprintsByProjectInbound {
    private SprintRepository sprintRepository;
    private ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Sprint> get(UUID userId, UUID projectId) {
        log.info("Get all sprints by project {}", projectId);
        if (!projectRepository.existsByIdAndUserId(projectId, userId)) {
            throw new AuthException("No auth for user %s in project %s".formatted(userId, projectId));
        }
        return sprintRepository.getAllByProjectId(projectId);
    }
}
