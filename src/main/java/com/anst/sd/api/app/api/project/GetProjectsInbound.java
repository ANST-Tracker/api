package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.Project;

import java.util.List;

public interface GetProjectsInbound {
  List<Project> get(Long userId);
}
