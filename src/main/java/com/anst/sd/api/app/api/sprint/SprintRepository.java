package com.anst.sd.api.app.api.sprint;

import com.anst.sd.api.domain.sprint.Sprint;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SprintRepository {
    Sprint save(Sprint sprint);

    Sprint getById(UUID uuid);

    Sprint getByIdAndProjectId(UUID uuid, UUID projectId);

    List<Sprint> getAllByProjectId(UUID projectId);
}
