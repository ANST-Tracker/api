package com.anst.sd.api.adapter.rest.users_projects.write.dto;

import com.anst.sd.api.domain.UsersProjects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsersProjectsDomainMapper {
    @Mapping(target = "project.id", source = "projectId")
    @Mapping(target = "user.id", source = "userId")
    UsersProjects mapToDomain(AddUserInProjectDto source);
}
