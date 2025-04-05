package com.anst.sd.api.adapter.rest.project.dto;

import com.anst.sd.api.adapter.rest.tag.dto.TagDtoMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserDtoMapper;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoDto;
import com.anst.sd.api.domain.UsersProjects;
import com.anst.sd.api.domain.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

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
    @Mapping(target = "headId", source = "head.id")
    public abstract ProjectInfoDto mapToDto(Project source);

    @Mapping(target = "headId", source = "head.id")
    public abstract List<ProjectInfoDto> mapToDto(List<Project> source);

    @Named("usersProjectsToUserInfoDto")
    public List<UserInfoDto> usersProjectsToUserInfoDto(List<UsersProjects> usersProjects) {
        if (usersProjects == null) {
            return null;
        }
        return usersProjects.stream()
                .map(UsersProjects::getUser)
                .map(user -> userDtoMapper.mapToDto(user))
                .collect(Collectors.toList());
    }
}
