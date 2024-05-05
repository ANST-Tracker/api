package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskJpaRepository extends JpaRepository<Task, Long>, PagingAndSortingRepository<Task, Long> {
    Page<Task> findTasksByProjectUserIdAndProjectId(Long userId, Long projectId, Pageable page);

    @Query("""
            select task from Task task
            join fetch task.project
            join fetch task.project.user
            where task.id = :id
            and task.project.user.id = :userId
            """)
    Optional<Task> findTaskByIdAndUserId(Long id, Long userId);
}
