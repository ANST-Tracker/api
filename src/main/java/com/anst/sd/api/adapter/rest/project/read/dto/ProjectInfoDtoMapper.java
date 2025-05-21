package com.anst.sd.api.adapter.rest.project.read.dto;

import com.anst.sd.api.adapter.rest.tag.dto.TagDtoMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserDtoMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoDto;
import com.anst.sd.api.domain.UsersProjects;
import com.anst.sd.api.domain.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
            UserDtoMapper.class,
            TagDtoMapper.class
})
public abstract class ProjectInfoDtoMapper {
    @Autowired
    protected UserDtoMapper userDtoMapper;

    @Mapping(target = "users", source = "users", qualifiedByName = "usersProjectsToUserInfoDto")
    @Mapping(target = "tags", source = "tags")
    public abstract ProjectInfoDto mapToDto(Project source);

    public abstract List<ProjectInfoDto> mapToDto(List<Project> source);

    @Named("usersProjectsToUserInfoDto")
    public List<UserInfoDto> usersProjectsToUserInfoDto(List<UsersProjects> usersProjects) {
        if (usersProjects == null) {
            return Collections.emptyList();
        }
        return usersProjects.stream()
                .map(UsersProjects::getUser)
                .map(userDtoMapper::mapToDto)
                .toList();
    }
}
