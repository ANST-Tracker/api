package com.anst.sd.api.app.api.sprint;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface AttachTaskToSprintInBound {
    void attach(UUID userId, UUID sprintId, String simpleId);
}
