package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskDomainMapper {
    Task mapToDomain(CreateTaskRequestDto source);

    Task mapToDomain(UpdateTaskRequestDto source);
}
