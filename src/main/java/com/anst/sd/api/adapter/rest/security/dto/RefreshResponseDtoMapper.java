package com.anst.sd.api.adapter.rest.security.dto;

import com.anst.sd.api.security.RefreshResponse;
import org.mapstruct.Mapper;

@Mapper
public interface RefreshResponseDtoMapper {

    RefreshResponseDto mapToDto(RefreshResponse source);
}
