package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.UpdateAbstractTaskStatusInBound;
import com.anst.sd.api.domain.task.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateAbstractTaskStatusUseCase implements UpdateAbstractTaskStatusInBound {
    private final AbstractTaskRepository abstractTaskRepository;

    @Override
    @Transactional
    public AbstractTask updateStatus(UUID userId, UUID taskId, String status) {
        AbstractTask task = abstractTaskRepository.findById(taskId);
        log.info("Updating task status for taskId {} to new status: {}", task, status);
        if (task instanceof StoryTask storyTask) {
            FullCycleStatus statusEnum = FullCycleStatus.valueOf(status);
            storyTask.setStatus(statusEnum);
        }
        if (task instanceof DefectTask defectTask) {
            FullCycleStatus statusEnum = FullCycleStatus.valueOf(status);
            defectTask.setStatus(statusEnum);
        }
        if (task instanceof Subtask subtask) {
            ShortCycleStatus statusEnum = ShortCycleStatus.valueOf(status);
            subtask.setStatus(statusEnum);
        }
        if (task instanceof EpicTask epicTask) {
            ShortCycleStatus statusEnum = ShortCycleStatus.valueOf(status);
            epicTask.setStatus(statusEnum);
        }
        return abstractTaskRepository.save(task);
    }
}
