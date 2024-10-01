package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.adapter.rest.task.write.dto.CreateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskDto;
import com.anst.sd.api.domain.task.Task;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(TaskDomainMapperDecorator.class)
public interface TaskDomainMapper {
    @Mapping(target = "tags", ignore = true)
    Task mapToDomain(CreateTaskDto source);

    @Mapping(target = "tags", ignore = true)
    Task mapToDomain(UpdateTaskDto source);
}
