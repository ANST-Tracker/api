package com.anst.sd.api.dao;

import com.anst.sd.api.model.entity.Role;
import com.anst.sd.api.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
