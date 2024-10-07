package com.anst.sd.api.app.  impl.task;

import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.app.api.task.UpdateOrderNumberTaskInBound;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateOrderNumberTaskUseCase implements UpdateOrderNumberTaskInBound {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task updateOrderNumber(Long userId, Long taskId, double updatedOrderNumber) {
        log.info("Updating number order task with id {} and userId {}", taskId, userId);
        Task task = taskRepository.findByIdAndUser(taskId, userId);
        task.setOrderNumber(updatedOrderNumber);
        return taskRepository.save(task);
    }


}
