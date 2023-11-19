package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.ClientException;
import com.anst.sd.api.app.api.task.GetTaskByUserInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.anst.sd.api.app.api.ClientErrorMessages.USER_DOESNT_HAVE_CURRENT_TASK;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTaskByUserUseCase implements GetTaskByUserInBound {
    private final TaskRepository taskRepository;

    @Override
    public Optional<Task> getTask(Long userId, Long id) {
        log.info("Get task by userId {}", userId);
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            log.debug("Task id {} has been received by userId {}",id,userId);
            return task;
        } else {
            log.warn("User does not have task with id {}", id);
            throw new ClientException(USER_DOESNT_HAVE_CURRENT_TASK);
        }
    }
}
