package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.user.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskRegistryDtoMapper {
    @Mapping(source = "status.value", target = "status.value")
    @Mapping(source = "type.value", target = "type.value")
    @Mapping(source = "priority.value", target = "priority.value")
    TaskRegistryDto mapToDto(AbstractTask abstractTask);

    @AfterMapping
    default void afterMapping(@MappingTarget TaskRegistryDto taskRegistryDto, AbstractTask abstractTask) {
        taskRegistryDto.getStatus().setCode(abstractTask.getStatus().name());
        taskRegistryDto.getType().setCode(abstractTask.getType().name());
        taskRegistryDto.getPriority().setCode(abstractTask.getPriority().name());
        taskRegistryDto.setCreatorFullName(getFullName(abstractTask.getCreator()));
        taskRegistryDto.setAssigneeFullName(getFullName(abstractTask.getAssignee()));
    }

    private static String getFullName(User user) {
        return user.getLastName() + " " + user.getFirstName();
    }
}
