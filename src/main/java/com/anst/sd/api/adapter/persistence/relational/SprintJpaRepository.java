package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.sprint.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SprintJpaRepository extends JpaRepository<Sprint, UUID> {
    @Query("""
        select s from Sprint s
        left join s.project p
        where s.project.id = :projectId
        """)
    List<Sprint> findAllByProjectId(UUID projectId);
}
