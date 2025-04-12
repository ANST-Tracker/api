package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Mapper(componentModel = "spring")
public interface TaskInfoDtoMapper {
    SubtaskInfoDto mapToDto(Subtask subtask);

    DefectTaskInfoDto mapToDto(DefectTask task);

    EpicTaskInfoDto mapToDto(EpicTask task);

    StoryTaskInfoDto mapToDto(StoryTask task);

    default Object mapToDtoAuto(AbstractTask task) {
        if (task instanceof StoryTask storyTask) {
            return mapToDto(storyTask);
        } else if (task instanceof DefectTask defectTask) {
            return mapToDto(defectTask);
        } else if (task instanceof EpicTask epicTask) {
            return mapToDto(epicTask);
        } else if (task instanceof Subtask subtask) {
            return mapToDto(subtask);
        } else {
            return null;
        }
    }

    @AfterMapping
    default void mapToDtoTimes(AbstractTask task, @MappingTarget AbstractTaskInfoDto dto) {
        if (task.getTimeEstimation() == null) {
            return;
        }
        Duration estimated = Duration.of(task.getTimeEstimation().getAmount(),
                task.getTimeEstimation().getTimeUnit().toChronoUnit());
        Duration used = task.getLogs()
                .stream()
                .map(log -> Duration.of(log.getTimeEstimation().getAmount(),
                        log.getTimeEstimation().getTimeUnit().toChronoUnit()))
                .reduce(Duration.ZERO, Duration::plus);
        Duration remaining = estimated.minus(used);
        dto.setTimeUsed(new TimeEstimationDto()
                .setAmount(((double) used.getSeconds()) / 3600)
                .setTimeUnit(TimeUnit.HOURS));
        dto.setTimeRemaining(new TimeEstimationDto()
                .setAmount(((double) remaining.getSeconds()) / 3600)
                .setTimeUnit(TimeUnit.HOURS));
    }

    @AfterMapping
    default void mapSimpleDictionaries(AbstractTask task, @MappingTarget AbstractTaskInfoDto dto) {
        dto.setStatus(new SimpleDictionary()
                .setCode(task.getStatus().name())
                .setValue(task.getStatus().getValue()));
        dto.setType(new SimpleDictionary()
                .setCode(task.getType().name())
                .setValue(task.getType().getValue()));
        if (task.getPriority() != null) {
            dto.setPriority(new SimpleDictionary()
                    .setCode(task.getPriority().name())
                    .setValue(task.getPriority().getValue()));
        }
    }
}
