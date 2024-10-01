package com.anst.sd.api.adapter.telegram.dto;

import com.anst.sd.api.domain.security.UserCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateUserCodeDtoMapper {
  CreateUserCodeDto mapToDto(UserCode userCode);
}
