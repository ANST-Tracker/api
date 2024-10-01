package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.security.Role;
import com.anst.sd.api.security.domain.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
