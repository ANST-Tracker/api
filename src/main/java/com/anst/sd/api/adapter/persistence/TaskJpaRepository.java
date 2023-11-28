package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskJpaRepository extends JpaRepository<Task, Long>, PagingAndSortingRepository<Task, Long> {
    Page<Task> findTasksByUserId(Long userId, Pageable page);

    Page<Task> findTasksByUserIdAndStatusIn(Long userId, List<TaskStatus> status, Pageable page);

    Optional<Task> findTaskByIdAndUserId(Long id, Long userId);
}
