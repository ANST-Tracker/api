package com.anst.sd.api.adapter.rest.security.dto;

import com.anst.sd.api.domain.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpRequestDomainMapper {
    User mapToDomain(SignupRequestDto source);
}
