package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.security.RoleRepository;
import com.anst.sd.api.domain.security.Role;
import com.anst.sd.api.security.domain.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    private final RoleJpaRepository roleJpaRepository;

    @Override
    public Optional<Role> findByName(ERole name) {
        return roleJpaRepository.findByName(name);
    }
}
