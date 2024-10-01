package com.anst.sd.api.app.impl.project;

import com.anst.sd.api.app.api.project.CreateProjectInbound;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.ProjectType;
import com.anst.sd.api.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class CreateProjectUseCase implements CreateProjectInbound {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public Project create(Project project, Long userId) {
    log.info("Creating project for userId {}", userId);
    User user = userRepository.getById(userId);
    project.setUser(user);
    project.setProjectType(ProjectType.BASE);
    return projectRepository.save(project);
  }
}
