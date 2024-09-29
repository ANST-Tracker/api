package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskDtoMapper {
    @Mapping(source = "tags", target = "tags")
    TaskInfoDto mapToDto(Task source);

    List<TaskInfoDto> mapToDto(List<Task> source);
}
