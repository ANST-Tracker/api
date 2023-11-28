package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    User getById(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    User save(User user);

    void deleteById(Long id);
}
