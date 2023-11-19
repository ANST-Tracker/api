package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.app.api.task.CreateTaskRequest;
import com.anst.sd.api.domain.task.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskDomainMapper {
    Task mapToDomain(CreateTaskRequest source);

    Task mapToDomain(UpdateTaskRequestDto source);
}
