package com.anst.sd.api.app.api;

import com.anst.sd.api.domain.Role;
import com.anst.sd.api.security.ERole;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(ERole name);
}