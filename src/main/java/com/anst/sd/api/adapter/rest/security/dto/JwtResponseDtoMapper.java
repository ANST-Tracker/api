package com.anst.sd.api.adapter.rest.security.dto;

import com.anst.sd.api.app.api.security.JwtResponse;
import org.mapstruct.Mapper;

@Mapper
public interface JwtResponseDtoMapper {
    JwtResponseDto mapToDto(JwtResponse source);
}
