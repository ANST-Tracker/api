package com.anst.sd.api.adapter.rest.security.dto;

import com.anst.sd.api.domain.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignupRequestDomainMapper {
    User mapToDomain(SignupRequestDto source);
}
