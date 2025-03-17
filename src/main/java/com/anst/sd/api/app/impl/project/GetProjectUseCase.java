package com.anst.sd.api.app.impl.project;

import com.anst.sd.api.app.api.project.GetProjectInbound;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class GetProjectUseCase implements GetProjectInbound {
    private ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public Project get(UUID id) {
        log.info("Getting project by Id {}", id);
        return projectRepository.getById(id);
    }
}
