package com.anst.sd.api.app.api.usersProjects;

import java.util.UUID;

public interface RemoveUserFromProjectInBound {
    void remove(UUID projectId, UUID userId, UUID adminUserId);
}
