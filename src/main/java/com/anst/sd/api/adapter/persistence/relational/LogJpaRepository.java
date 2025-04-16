package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LogJpaRepository extends JpaRepository<Log, UUID> {
    @Query("""
        select l from Log l
        left join l.task t
        left join t.project p
        where p.id = :projectId
        and t.simpleId = :taskId
    """)
    List<Log> findAllByTaskAndProject(String taskId, UUID projectId);

    @Query("""
        select l from Log l
        left join l.task t
        left join t.project p
        left join fetch l.user u
        where p.id = :projectId
        and t.simpleId = :taskId
        and l.id = :id
        and u.id = :userId
    """)
    Optional<Log> findByIdAndTaskAndProjectAndUser(UUID id, String taskId, UUID projectId, UUID userId);

    @Query("""
    select l from Log l
    left join fetch l.task t
    left join fetch t.project p
    where p.id = :projectId
    and l.user.id = :userId
    and l.created between :start and :end          
    """)
    List<Log>  findAllByPeriodAndProjectAndUser(UUID projectId, UUID userId, Instant start, Instant end);

    @Query("""
    select l from Log l
    left join fetch l.task t
    left join fetch t.project p
    where l.user.id = :userId
    and l.created between :start and :end          
    """)
    List<Log>  findAllByPeriodAndUser(UUID userId, Instant start, Instant end);
}
