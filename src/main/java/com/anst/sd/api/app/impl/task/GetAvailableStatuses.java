package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.GetAvailableStatusesInBound;
import com.anst.sd.api.domain.task.FullCycleStatus;
import com.anst.sd.api.domain.task.ShortCycleStatus;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetAvailableStatuses implements GetAvailableStatusesInBound {

    private final Map<FullCycleStatus, List<FullCycleStatus>> fullCycleTransitions = new EnumMap<>(FullCycleStatus.class);
    private final Map<ShortCycleStatus, List<ShortCycleStatus>> shortCycleTransitions = new EnumMap<>(ShortCycleStatus.class);

    public GetAvailableStatuses() {
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

    public Map<String, List<String>> getAvailableStatusesForFullCycle() {
        return mapToResponse(fullCycleTransitions);
    }

    public Map<String, List<String>> getAvailableStatusesForShortCycle() {
        return mapToResponse(shortCycleTransitions);
    }

    private <T extends Enum<T>> Map<String, List<String>> mapToResponse(Map<T, List<T>> transitions) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        transitions.forEach((status, allowedTransitions) ->
                result.put(status.name(), allowedTransitions.stream()
                        .map(Enum::name)
                        .toList())
        );
        return result;
    }
}
