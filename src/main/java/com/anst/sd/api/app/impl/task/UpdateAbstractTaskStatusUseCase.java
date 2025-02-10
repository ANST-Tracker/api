package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.adapter.rest.task.write.dto.UpdateAbstractTaskStatusDto;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.UpdateAbstractTaskStatusInBound;
import com.anst.sd.api.domain.task.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateAbstractTaskStatusUseCase implements UpdateAbstractTaskStatusInBound {
    private final AbstractTaskRepository abstractTaskRepository;

    @Override
    public AbstractTask updateStatus(UUID userId, UUID taskId, UpdateAbstractTaskStatusDto request) {
        AbstractTask task = abstractTaskRepository.findById(taskId);

        if (task instanceof StoryTask storyTask) {
            FullCycleStatus statusEnum = FullCycleStatus.valueOf(request.getStatus());
            storyTask.setStatus(statusEnum);
        }
        if (task instanceof DefectTask defectTask) {
            FullCycleStatus statusEnum = FullCycleStatus.valueOf(request.getStatus());
            defectTask.setStatus(statusEnum);
        }
        if (task instanceof Subtask subtask) {
            ShortCycleStatus statusEnum = ShortCycleStatus.valueOf(request.getStatus());
            subtask.setStatus(statusEnum);
        }
        if (task instanceof EpicTask epicTask) {
            ShortCycleStatus statusEnum = ShortCycleStatus.valueOf(request.getStatus());
            epicTask.setStatus(statusEnum);
        }

        return abstractTaskRepository.save(task);
    }
}
