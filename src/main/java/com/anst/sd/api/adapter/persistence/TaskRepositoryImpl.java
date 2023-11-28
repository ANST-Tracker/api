package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.app.api.task.TaskNotFoundException;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {
    private final TaskJpaRepository taskJpaRepository;

    @Override
    public Page<Task> findTasksByUserId(Long userId, Pageable page) {
        return taskJpaRepository.findTasksByUserId(userId, page);
    }

    @Override
    public Page<Task> findTasksByUserIdAndStatusIn(Long userId, List<TaskStatus> status, Pageable page) {
        return taskJpaRepository.findTasksByUserIdAndStatusIn(userId, status, page);
    }

    @Override
    public Task save(Task task) {
        return taskJpaRepository.save(task);
    }

    @Override
    public Task findByIdAndUser(Long id, Long userId) {
        return taskJpaRepository.findTaskByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id, userId));
    }

    @Override
    public void deleteById(Long id) {
        taskJpaRepository.deleteById(id);
    }
}
