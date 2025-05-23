package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    @Query("""
        select up.user from UsersProjects up
        where up.user.id = :userId and up.project.id = :projectId
        """)
    Optional<User> findById(UUID userId, UUID projectId);

    @Query("""
        select u from User u
        where lower(concat(u.lastName, ' ', u.firstName)) like :fullNamePattern
        """)
    List<User> findAllByFullNameContains(String fullNamePattern);
}