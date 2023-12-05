package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.CreateTaskInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskUseCase implements CreateTaskInBound {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Task create(Long userId, Task task) {
        log.info("Create task by user with userId {}", userId);
        User user = userRepository.getById(userId);
        task.setUser(user);
        task.setCreated(task.getCreated());
        taskRepository.save(task);
        return task;
    }
}
