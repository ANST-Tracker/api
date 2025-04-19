package com.anst.sd.api.app.api.sprint;

import com.anst.sd.api.domain.sprint.Sprint;

import java.util.List;
import java.util.UUID;

public interface SprintRepository {
    List<Sprint> getAllByProjectId(UUID projectId);
}
