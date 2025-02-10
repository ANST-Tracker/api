package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.GetAvailableStatusesInBound;
import com.anst.sd.api.domain.task.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GetAvailableStatusesUseCase implements GetAvailableStatusesInBound {
    private final AbstractTaskRepository abstractTaskRepository;
    private final Map<FullCycleStatus, List<FullCycleStatus>> fullCycleTransitions = new EnumMap<>(FullCycleStatus.class);
    private final Map<ShortCycleStatus, List<ShortCycleStatus>> shortCycleTransitions = new EnumMap<>(ShortCycleStatus.class);

    public GetAvailableStatusesUseCase(AbstractTaskRepository abstractTaskRepository) {
        this.abstractTaskRepository = abstractTaskRepository;
        fullCycleTransitions.put(FullCycleStatus.OPEN, List.of(FullCycleStatus.IN_PROGRESS));
        fullCycleTransitions.put(FullCycleStatus.IN_PROGRESS, List.of(FullCycleStatus.REVIEW, FullCycleStatus.RESOLVED));
        fullCycleTransitions.put(FullCycleStatus.REVIEW, List.of(FullCycleStatus.RESOLVED, FullCycleStatus.QA_READY));
        fullCycleTransitions.put(FullCycleStatus.RESOLVED, List.of(FullCycleStatus.QA_READY, FullCycleStatus.CLOSED));
        fullCycleTransitions.put(FullCycleStatus.QA_READY, List.of(FullCycleStatus.IN_QA, FullCycleStatus.CLOSED));
        fullCycleTransitions.put(FullCycleStatus.IN_QA, List.of(FullCycleStatus.CLOSED));
        fullCycleTransitions.put(FullCycleStatus.CLOSED, List.of());
        shortCycleTransitions.put(ShortCycleStatus.OPEN, List.of(ShortCycleStatus.IN_PROGRESS));
        shortCycleTransitions.put(ShortCycleStatus.IN_PROGRESS, List.of(ShortCycleStatus.REVIEW, ShortCycleStatus.CLOSED));
        shortCycleTransitions.put(ShortCycleStatus.REVIEW, List.of(ShortCycleStatus.CLOSED));
        shortCycleTransitions.put(ShortCycleStatus.CLOSED, List.of());
    }

    @Override
    @Transactional
    public List<SimpleDictionary> getAppropriateStatuses(UUID taskId) {
        AbstractTask task = abstractTaskRepository.findById(taskId);

        if (task instanceof StoryTask story) {
            FullCycleStatus current = story.getStatus();
            List<FullCycleStatus> next = fullCycleTransitions.getOrDefault(current, List.of());
            return toSimpleDictionaryList(next);
        }
        if (task instanceof DefectTask defect) {
            FullCycleStatus current = defect.getStatus();
            List<FullCycleStatus> next = fullCycleTransitions.getOrDefault(current, List.of());
            return toSimpleDictionaryList(next);
        }
        if (task instanceof EpicTask epic) {
            ShortCycleStatus current = epic.getStatus();
            List<ShortCycleStatus> next = shortCycleTransitions.getOrDefault(current, List.of());
            return toSimpleDictionaryList(next);
        }
        if (task instanceof Subtask subtask) {
            ShortCycleStatus current = subtask.getStatus();
            List<ShortCycleStatus> next = shortCycleTransitions.getOrDefault(current, List.of());
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
