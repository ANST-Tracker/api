package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByTelegramId(String telegramId);

    Boolean existsByUsername(String username);
}
