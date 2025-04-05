package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.*;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.TaskType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetTaskUseCase implements GetTaskInbound {
    private final AbstractTaskRepository abstractTaskRepository;
    private final DefectTaskRepository defectTaskRepository;
    private final StoryTaskRepository storyTaskRepository;
    private final EpicTaskRepository epicTaskRepository;
    private final SubtaskRepository subtaskRepository;

    @Override
    @Transactional(readOnly = true)
    public AbstractTask get(String simpleId, UUID userId) {
        log.info("User {} getting task {}", userId, simpleId);
        TaskType type = abstractTaskRepository.getTypeBySimpleIdAndUserId(simpleId, userId);
        switch (type) {
            case STORY -> {
                return storyTaskRepository.getBySimpleId(simpleId);
            }
            case EPIC -> {
                return epicTaskRepository.getBySimpleId(simpleId);
            }
            case SUBTASK -> {
                return subtaskRepository.getBySimpleId(simpleId);
            }
            case DEFECT -> {
                return defectTaskRepository.getBySimpleId(simpleId);
            }
            default -> throw new AbstractTaskNotFound(simpleId);
        }
    }
}
