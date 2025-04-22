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
        select count(up.id) > 0 from UsersProjects up
        left join up.project p
        left join up.user u
        where p.id = :id
        and u.id = :userId
        """)
    boolean existsByIdAndUserId(UUID id, UUID userId);

    @Query("""
        select up.project from UsersProjects up
        left join up.user u
        where up.user.id = :userId    
        """)
    List<Project> findAllByUserId(UUID userId);
}
