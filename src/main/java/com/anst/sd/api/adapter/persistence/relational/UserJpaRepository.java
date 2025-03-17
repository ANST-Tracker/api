package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    //TODO: After UsersProjects need to create a query, which gets an userId with projectId from UsersProjects entity.
    Optional<User> findById(UUID id);
}