package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface TaskJpaRepository extends JpaRepository<Task, Long>, PagingAndSortingRepository<Task, Long> {
    Page<Task> findTasksByProjectUserIdAndProjectId(Long userId, Long projectId, Pageable page);

    @Query("""
            select task from Task task
            left join fetch task.project p
            left join fetch p.user u
            left join fetch task.pendingNotifications
            where task.id = :id
            and u.id = :userId
            """)
    Optional<Task> findTaskByIdAndUserId(Long id, Long userId);

    @Query("""
             select nextval('device_id_seq')
            """)
    BigInteger getNextOrderNumber();
}
