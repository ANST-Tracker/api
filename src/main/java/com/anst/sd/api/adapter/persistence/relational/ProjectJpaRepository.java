package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findById(UUID id);

    @Query("""
        select count(up.id) > 0 from UsersProjects up
        left join up.project p
        left join up.user u
        where p.id = :projectId
        and u.id = :userId
        """)
    boolean existsByIdAndUserId(UUID userId, UUID projectId);
}
