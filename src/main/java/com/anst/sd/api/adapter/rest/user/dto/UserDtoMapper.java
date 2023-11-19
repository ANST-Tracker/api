package com.anst.sd.api.adapter.rest.user.dto;

import com.anst.sd.api.domain.user.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserDtoMapper {
    UserInfoResponse mapToDto(User source);
}
