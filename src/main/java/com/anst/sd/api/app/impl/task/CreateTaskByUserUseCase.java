package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.CreateTaskByUserInBound;
import com.anst.sd.api.app.api.task.GetUserInTaskInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskByUserUseCase implements CreateTaskByUserInBound {
    private final TaskRepository taskRepository;
    private final GetUserInTaskInBound getUserInTaskInBound;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Task createTask(Long userId, Task task) {
        log.info("Create task by user with userId {}", userId);
        User user = getUserInTaskInBound.getUser(userId);
        task.setUser(user);
        task.setCreated(task.getCreated());
        taskRepository.save(task);
        log.debug("Task has been created with id {}", task.getId());
        return task;
    }
}
