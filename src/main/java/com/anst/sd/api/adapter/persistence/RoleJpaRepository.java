package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.domain.Role;
import com.anst.sd.api.app.api.user.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
