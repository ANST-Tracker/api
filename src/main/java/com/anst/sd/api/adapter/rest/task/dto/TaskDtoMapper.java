package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskDtoMapper {
  TaskInfoDto mapToDto(Task source);

  List<TaskInfoDto> mapToDto(List<Task> source);
}
