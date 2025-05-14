package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.UsersProjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersProjectsJpaRepository extends JpaRepository<UsersProjects, UUID> {
    List<UsersProjects> findByProjectId(UUID projectId);

    @Query("""
        select up from UsersProjects up
        left join up.user u
        left join up.project p
        where p.id = :projectId
        and (up.user.id = :userId or p.head.id = :userId)
        """)
    Optional<UsersProjects> findByUserIdAndProjectId(UUID userId, UUID projectId);
}
