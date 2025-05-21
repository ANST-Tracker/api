package com.anst.sd.api.app.api.users_projects;

import java.util.UUID;

public interface RemoveUserFromProjectInBound {
    void remove(UUID projectId, UUID userId, UUID adminUserId);
}
