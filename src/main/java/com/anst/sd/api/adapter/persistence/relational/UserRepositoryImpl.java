package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.user.UserNotFoundException;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User getByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public User getById(UUID id) {
        return userJpaRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public List<User> findByNameFragment(String nameFragment) {
        return userJpaRepository.findAllByFullNameContains(format("%%%s%%", nameFragment));
    }
}
