package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;
import com.anst.sd.api.adapter.rest.task.dto.TaskMapper;
import com.anst.sd.api.app.api.ClientException;
import com.anst.sd.api.app.api.task.DeleteTaskByUserInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.anst.sd.api.app.api.ClientErrorMessages.USER_DOESNT_HAVE_CURRENT_TASK;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteTaskByUserUseCase implements DeleteTaskByUserInBound {
    private final TaskRepository taskRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TaskInfo deleteTask(Long userId, Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent() && task.get().getUser().getId().equals(userId)) {
            taskRepository.deleteById(id);
            return TaskMapper.toApi(task.get());
        } else {
            throw new ClientException(USER_DOESNT_HAVE_CURRENT_TASK);
        }
    }
}
