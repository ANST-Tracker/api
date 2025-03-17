package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.GetAvailableStatusesInBound;
import com.anst.sd.api.domain.task.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GetAvailableStatusesUseCase implements GetAvailableStatusesInBound {
    private static final Map<Class<? extends AbstractTask>, Map<TaskStatus, List<TaskStatus>>> TRANSITIONS = new HashMap<>();

    static {
        Map<TaskStatus, List<TaskStatus>> fullCycleTransitions = new EnumMap<>(TaskStatus.class);
        fullCycleTransitions.put(TaskStatus.OPEN, List.of(TaskStatus.IN_PROGRESS));
        fullCycleTransitions.put(TaskStatus.IN_PROGRESS, List.of(TaskStatus.REVIEW, TaskStatus.OPEN));
        fullCycleTransitions.put(TaskStatus.REVIEW, List.of(TaskStatus.RESOLVED, TaskStatus.OPEN));
        fullCycleTransitions.put(TaskStatus.RESOLVED, List.of(TaskStatus.QA_READY, TaskStatus.OPEN));
        fullCycleTransitions.put(TaskStatus.QA_READY, List.of(TaskStatus.IN_QA));
        fullCycleTransitions.put(TaskStatus.IN_QA, List.of(TaskStatus.CLOSED, TaskStatus.OPEN));
        fullCycleTransitions.put(TaskStatus.CLOSED, List.of());
        TRANSITIONS.put(StoryTask.class, fullCycleTransitions);
        TRANSITIONS.put(DefectTask.class, fullCycleTransitions);

        Map<TaskStatus, List<TaskStatus>> shortCycleTransitions = new EnumMap<>(TaskStatus.class);
        shortCycleTransitions.put(TaskStatus.OPEN, List.of(TaskStatus.IN_PROGRESS));
        shortCycleTransitions.put(TaskStatus.IN_PROGRESS, List.of(TaskStatus.REVIEW, TaskStatus.OPEN));
        shortCycleTransitions.put(TaskStatus.REVIEW, List.of(TaskStatus.CLOSED));
        shortCycleTransitions.put(TaskStatus.CLOSED, List.of());
        TRANSITIONS.put(EpicTask.class, shortCycleTransitions);
        TRANSITIONS.put(Subtask.class, shortCycleTransitions);
    }

    private final AbstractTaskRepository abstractTaskRepository;

    public GetAvailableStatusesUseCase(AbstractTaskRepository abstractTaskRepository) {
        this.abstractTaskRepository = abstractTaskRepository;
    }

    @Override
    @Transactional
    public List<SimpleDictionary> getAppropriateStatuses(String simpleId) {
        log.info("Getting appropriate statuses for task simpleId {}", simpleId);
        AbstractTask task = abstractTaskRepository.getBySimpleId(simpleId);

        Map<TaskStatus, List<TaskStatus>> transitions = TRANSITIONS.get(task.getClass());
        if (transitions != null) {
            List<TaskStatus> nextStatuses = transitions.getOrDefault(task.getStatus(), List.of());
            return toSimpleDictionaryList(nextStatuses);
        }
        log.warn("No transitions defined for task type {}", task.getClass().getSimpleName());
        return List.of();
    }

    private List<SimpleDictionary> toSimpleDictionaryList(List<TaskStatus> statuses) {
        return statuses.stream()
                .map(status -> {
                    SimpleDictionary sd = new SimpleDictionary();
                    sd.setCode(status.name());
                    sd.setValue(status.getValue());
                    return sd;
                })
                .toList();
    }
}