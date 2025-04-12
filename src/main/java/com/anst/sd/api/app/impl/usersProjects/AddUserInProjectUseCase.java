package com.anst.sd.api.app.impl.usersProjects;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.app.api.usersProjects.AddUserInProjectInBound;
import com.anst.sd.api.app.api.usersProjects.UsersProjectsRepository;
import com.anst.sd.api.app.api.usersProjects.UsersProjectsValidationException;
import com.anst.sd.api.domain.UsersProjects;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AddUserInProjectUseCase implements AddUserInProjectInBound {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UsersProjectsRepository usersProjectsRepository;

    @Override
    @Transactional
    public UsersProjects add(UsersProjects usersProjects, UUID adminUserId) {
        log.info("Adding user {} to project {} by admin {}",
                usersProjects.getUser().getId(),
                usersProjects.getProject().getId(),
                adminUserId);

        if (usersProjectsRepository.existsByUserIdAndProjectId(usersProjects.getUser().getId(), usersProjects.getProject().getId())) {
            throw new UsersProjectsValidationException();
        }

        User user = userRepository.getById(usersProjects.getUser().getId());
        Project project = projectRepository.getById(usersProjects.getProject().getId());
        usersProjects.setUser(user);
        usersProjects.setProject(project);

        return usersProjectsRepository.save(usersProjects);
    }
}
