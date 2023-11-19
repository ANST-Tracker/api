package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {
    private final TaskJpaRepositoryOutBound repository;

    @Override
    public Page<Task> findTasksByUserId(Long userId, Pageable page) {
        return repository.findTasksByUserId(userId,page);
    }

    @Override
    public Page<Task> findTasksByUserIdAndStatusIn(Long userId, List<TaskStatus> status, Pageable page) {
        return repository.findTasksByUserIdAndStatusIn(userId,status,page);
    }

    @Override
    public Task save(Task task) {
        return repository.save(task);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
    }
}
