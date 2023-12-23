package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

public interface UserRepository {
    User getByUsername(String username);

    User getById(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User save(User user);

    void deleteById(Long id);
}
