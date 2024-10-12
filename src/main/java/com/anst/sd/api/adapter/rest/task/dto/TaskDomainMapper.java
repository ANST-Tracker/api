package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.adapter.rest.task.write.dto.CreateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskDto;
import com.anst.sd.api.domain.task.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskDomainMapper {
    Task mapToDomain(CreateTaskDto source);

    Task mapToDomain(UpdateTaskDto source);
}
