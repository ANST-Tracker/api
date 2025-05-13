package com.anst.sd.api.app.api.user;

import com.anst.sd.api.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User getByUsername(String username);

    User getById(UUID id);

    User save(User user);

    List<User> findByNameFragment(String nameFragment);
}
