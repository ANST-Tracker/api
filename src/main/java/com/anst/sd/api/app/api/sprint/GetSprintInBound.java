package com.anst.sd.api.app.api.sprint;

import com.anst.sd.api.domain.sprint.Sprint;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface GetSprintInBound {
    Sprint get(UUID sprintId, UUID userId);
}
