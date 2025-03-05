package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.GetAvailableStatusesInBound;
import com.anst.sd.api.app.api.task.UpdateAbstractTaskStatusInBound;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.SimpleDictionary;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateAbstractTaskStatusUseCase implements UpdateAbstractTaskStatusInBound {
    private final AbstractTaskRepository abstractTaskRepository;
    private final GetAvailableStatusesInBound getAvailableStatusesInBound;

    @Override
    @Transactional
    public AbstractTask updateStatus(UUID userId, String simpleId, String status) {
        log.info("Updating task status for taskId {} to new status: {}", simpleId, status);
        AbstractTask task = abstractTaskRepository.findBySimpleId(simpleId);
        if (task == null) {
            throw new IllegalArgumentException("Task with simpleId " + simpleId + " not found");
        }
        TaskStatus newStatus;
        try {
            newStatus = TaskStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        List<SimpleDictionary> availableStatuses = getAvailableStatusesInBound.getAppropriateStatuses(simpleId);
        boolean isValidTransition = availableStatuses.stream()
                .anyMatch(sd -> sd.getCode().equals(status));
        if (!isValidTransition) {
            throw new IllegalStateException("Cannot transition to status " + status + " from current status " + task.getStatus());
        }
        task.setStatus(newStatus);
        return abstractTaskRepository.save(task);
    }
}