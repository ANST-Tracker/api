package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.GetAvailableStatusesInBound;
import com.anst.sd.api.domain.task.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GetAvailableStatusesUseCase implements GetAvailableStatusesInBound {
    private final AbstractTaskRepository abstractTaskRepository;
    private static final Map<FullCycleStatus, List<FullCycleStatus>> FULL_CYCLE_TRANSITIONS = new EnumMap<>(FullCycleStatus.class);
    private static final Map<ShortCycleStatus, List<ShortCycleStatus>> SHORT_CYCLE_TRANSITIONS = new EnumMap<>(ShortCycleStatus.class);

    public GetAvailableStatusesUseCase(AbstractTaskRepository abstractTaskRepository) {
        this.abstractTaskRepository = abstractTaskRepository;
        FULL_CYCLE_TRANSITIONS.put(FullCycleStatus.OPEN, List.of(FullCycleStatus.IN_PROGRESS));
        FULL_CYCLE_TRANSITIONS.put(FullCycleStatus.IN_PROGRESS, List.of(FullCycleStatus.REVIEW, FullCycleStatus.OPEN));
        FULL_CYCLE_TRANSITIONS.put(FullCycleStatus.REVIEW, List.of(FullCycleStatus.RESOLVED, FullCycleStatus.OPEN));
        FULL_CYCLE_TRANSITIONS.put(FullCycleStatus.RESOLVED, List.of(FullCycleStatus.QA_READY, FullCycleStatus.OPEN));
        FULL_CYCLE_TRANSITIONS.put(FullCycleStatus.QA_READY, List.of(FullCycleStatus.IN_QA));
        FULL_CYCLE_TRANSITIONS.put(FullCycleStatus.IN_QA, List.of(FullCycleStatus.CLOSED, FullCycleStatus.OPEN));
        FULL_CYCLE_TRANSITIONS.put(FullCycleStatus.CLOSED, List.of());
        SHORT_CYCLE_TRANSITIONS.put(ShortCycleStatus.OPEN, List.of(ShortCycleStatus.IN_PROGRESS));
        SHORT_CYCLE_TRANSITIONS.put(ShortCycleStatus.IN_PROGRESS, List.of(ShortCycleStatus.REVIEW, ShortCycleStatus.OPEN));
        SHORT_CYCLE_TRANSITIONS.put(ShortCycleStatus.REVIEW, List.of(ShortCycleStatus.CLOSED));
        SHORT_CYCLE_TRANSITIONS.put(ShortCycleStatus.CLOSED, List.of());
    }

    @Override
    @Transactional
    public List<SimpleDictionary> getAppropriateStatuses(String simpleId) {
        log.info("Getting appropriate statuses for task simpleId {}", simpleId);
        AbstractTask task = abstractTaskRepository.findBySimpleId(simpleId);
        if (task instanceof StoryTask story) {
            FullCycleStatus current = story.getStatus();
            List<FullCycleStatus> next = FULL_CYCLE_TRANSITIONS.getOrDefault(current, List.of());
            return toSimpleDictionaryList(next);
        }
        if (task instanceof DefectTask defect) {
            FullCycleStatus current = defect.getStatus();
            List<FullCycleStatus> next = FULL_CYCLE_TRANSITIONS.getOrDefault(current, List.of());
            return toSimpleDictionaryList(next);
        }
        if (task instanceof EpicTask epic) {
            ShortCycleStatus current = epic.getStatus();
            List<ShortCycleStatus> next = SHORT_CYCLE_TRANSITIONS.getOrDefault(current, List.of());
            return toSimpleDictionaryList(next);
        }
        if (task instanceof Subtask subtask) {
            ShortCycleStatus current = subtask.getStatus();
            List<ShortCycleStatus> next = SHORT_CYCLE_TRANSITIONS.getOrDefault(current, List.of());
            return toSimpleDictionaryList(next);
        }
        return List.of();
    }

    private List<SimpleDictionary> toSimpleDictionaryList(List<? extends Enum<?>> statuses) {
        return statuses.stream()
                .map(enumVal -> {
                    SimpleDictionary sd = new SimpleDictionary();
                    sd.setCode(enumVal.name());
                    if (enumVal instanceof FullCycleStatus f) {
                        sd.setValue(f.getValue());
                    }
                    if (enumVal instanceof ShortCycleStatus s) {
                        sd.setValue(s.getValue());
                    }
                    return sd;
                })
                .toList();
    }
}