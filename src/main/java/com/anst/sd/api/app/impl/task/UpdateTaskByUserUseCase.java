package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;
import com.anst.sd.api.adapter.rest.task.dto.TaskMapper;
import com.anst.sd.api.app.api.ClientException;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.app.api.task.UpdateTaskByUserInBound;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.anst.sd.api.app.api.ClientErrorMessages.USER_DOESNT_HAVE_CURRENT_TASK;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateTaskByUserUseCase implements UpdateTaskByUserInBound {
    private final TaskRepository taskRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TaskInfo updateTask(Long userId, Task request) {
        Optional<Task> task = taskRepository.findById(request.getId());
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            Task existingTask = task.get();
            existingTask.setData(request.getData());
            existingTask.setModified(LocalDateTime.now());
            existingTask.setDescription(request.getDescription());
            existingTask.setStatus(request.getStatus());
            var result = taskRepository.save(existingTask);
            return TaskMapper.toApi(result);
        } else {
            throw new ClientException(USER_DOESNT_HAVE_CURRENT_TASK);
        }
    }
}
