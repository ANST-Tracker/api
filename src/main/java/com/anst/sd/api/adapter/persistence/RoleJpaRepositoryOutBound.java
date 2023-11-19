package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.domain.Role;
import com.anst.sd.api.security.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepositoryOutBound extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
