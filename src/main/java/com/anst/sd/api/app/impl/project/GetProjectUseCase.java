package com.anst.sd.api.app.impl.project;

import com.anst.sd.api.app.api.project.GetProjectInbound;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.usersProjects.UsersProjectsRepository;
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
    private TagRepository tagRepository;
    private UsersProjectsRepository usersProjectsRepository;

    @Override
    @Transactional(readOnly = true)
    public Project get(UUID projectId, UUID userId) {
        log.info("Getting project information by Id {}, by user {}", projectId, userId);

        Project project = projectRepository.getByIdAndUserId(projectId, userId);
        project.setTags(tagRepository.findAllByProjectId(project.getId()));
        project.setUsers(usersProjectsRepository.findByProjectId(project.getId()));

        return project;
    }
}
