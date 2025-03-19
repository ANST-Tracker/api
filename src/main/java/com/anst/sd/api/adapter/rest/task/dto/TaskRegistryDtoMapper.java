package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.user.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskRegistryDtoMapper {
    TaskRegistryDto mapToDto(AbstractTask abstractTask);

    @AfterMapping
    default void afterMapping(@MappingTarget TaskRegistryDto taskRegistryDto, AbstractTask abstractTask) {
        taskRegistryDto.setCreatorFullName(getFullName(abstractTask.getCreator()));
        taskRegistryDto.setAssigneeFullName(getFullName(abstractTask.getAssignee()));
    }

    private static String getFullName(User user) {
        return user.getLastName() + " " + user.getFirstName();
    }
}
