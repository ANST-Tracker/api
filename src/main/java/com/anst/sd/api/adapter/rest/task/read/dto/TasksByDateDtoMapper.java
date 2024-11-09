package com.anst.sd.api.adapter.rest.task.read.dto;

import com.anst.sd.api.domain.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = TaskCalendarInfoDtoMapper.class)
public interface TasksByDateDtoMapper {
    @Mapping(target = "date", source = "entry.key")
    @Mapping(target = "tasks", source = "entry.value")
    TasksByDateDto mapToDto(Map.Entry<LocalDate, List<Task>> entry);

    default List<TasksByDateDto> mapToDto(Map<LocalDate, List<Task>> tasksByDate) {
        return tasksByDate.entrySet().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}