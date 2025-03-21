package com.anst.sd.api.app.api.usersProjects;

import com.anst.sd.api.domain.UsersProjects;

import java.util.UUID;

public interface AddUserInProjectInBound {
    UsersProjects add(UsersProjects usersProjects, UUID adminUserId);
}
