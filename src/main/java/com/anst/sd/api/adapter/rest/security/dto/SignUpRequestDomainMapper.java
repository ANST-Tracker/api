package com.anst.sd.api.adapter.rest.security.dto;

import com.anst.sd.api.domain.user.User;

public interface SignUpRequestDomainMapper {
    User mapToDomain(SignupRequestDto source);
}
