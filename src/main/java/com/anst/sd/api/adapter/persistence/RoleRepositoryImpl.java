package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.app.api.RoleRepository;
import com.anst.sd.api.domain.Role;
import com.anst.sd.api.security.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    private final RoleJpaRepository repository;

    @Override
    public Optional<Role> findByName(ERole name) {
        return repository.findByName(name);
    }
}
