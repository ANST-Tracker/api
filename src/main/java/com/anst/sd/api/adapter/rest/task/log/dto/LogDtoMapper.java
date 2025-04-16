package com.anst.sd.api.adapter.rest.task.log.dto;

import com.anst.sd.api.domain.task.Log;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LogDtoMapper {
    @Mapping(source = "task.simpleId", target = "taskSimpleId")
    @Mapping(source = "task.project.name", target = "projectName")
    @Mapping(source = "task.project.id", target = "projectId")
    TimeSheetDto mapToDto(Log log);

    List<TimeSheetDto> mapToDto(List<Log> logs);
}
