package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.*;
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
        AbstractTask task = abstractTaskRepository.getBySimpleId(simpleId);
        TaskStatus newStatus;
        try {
            newStatus = TaskStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new AbstractTaskValidationException("Invalid status: " + status);
        }

        List<SimpleDictionary> availableStatuses = getAvailableStatusesInBound.getAppropriateStatuses(simpleId);
        boolean isValidTransition = availableStatuses.stream()
                .anyMatch(sd -> sd.getCode().equals(status));
        if (!isValidTransition) {
            throw new AbstractTaskAppropriateStatusException(status, task.getStatus().toString());
        }
        task.setStatus(newStatus);
        return abstractTaskRepository.save(task);
    }
}