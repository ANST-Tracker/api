package com.anst.sd.api.app.impl.project;

import com.anst.sd.api.app.api.project.GetProjectsInbound;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GetProjectsUseCase implements GetProjectsInbound {
  private ProjectRepository projectRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Project> get(Long userId) {
    log.info("Getting projects by userId {}", userId);
    return projectRepository.findByUserId(userId);
  }
}
