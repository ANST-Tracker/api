package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, UUID> {
    @NonNull Optional<Project> findById(@NonNull UUID id);

    @Query("""
        select count(p.id) > 0 from Project p
        left join p.users up
        left join up.user u
        where p.id = :id
        and (u.id = :userId or p.head.id = :headId)
        """)
    boolean existsByIdAndUserId(UUID id, UUID userId, UUID headId);

    @Query("""
        select p from Project p
        left join p.users up
        where up.user.id = :userId
        or p.head.id = :userId
        """)
    List<Project> findAllByUserId(UUID userId);

    @Query("""
        select p from Project p
        left join p.users up
        left join up.user u
        left join p.head pHead
        where p.id = :projectId
        and (up.user.id = :userId or pHead.id = :userId)
        """)
    Optional<Project> findByUserIdAndProjectId(UUID userId, UUID projectId);
}
