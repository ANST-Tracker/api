package com.anst.sd.api.app.impl.project;

import com.anst.sd.api.app.api.project.GetProjectsByUserInbound;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class GetProjectsByUserUseCase implements GetProjectsByUserInbound {
    private ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Project> get(UUID userId) {
        log.info("Get all projects by user {}", userId);
        return projectRepository.getAllByUserId(userId);
    }
}
