package com.anst.sd.api.adapter.rest.user.dto;

import com.anst.sd.api.domain.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserInfoDto mapToDto(User source);
}