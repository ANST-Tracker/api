package com.anst.sd.api.app.api.security;

import com.anst.sd.api.domain.security.Role;
import com.anst.sd.api.security.domain.ERole;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(ERole name);
}
