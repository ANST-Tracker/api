package com.anst.sd.api.adapter.telegram;

import com.anst.sd.api.domain.security.UserCode;
import org.mapstruct.Mapper;

@Mapper
public interface CreateUserCodeDtoMapper {
    CreateUserCodeDto mapToDto(UserCode userCode);
}
