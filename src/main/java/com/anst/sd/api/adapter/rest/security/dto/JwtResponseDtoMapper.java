package com.anst.sd.api.adapter.rest.security.dto;

import com.anst.sd.api.security.JwtResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JwtResponseDtoMapper {
    JwtResponseDto mapToDto(JwtResponse source);
}
