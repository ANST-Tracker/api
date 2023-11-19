package com.anst.sd.api.adapter.rest.security.dto;

import com.anst.sd.api.app.api.security.SignupRequest;

public interface SignUpRequestDomainMapper {
    SignupRequest mapToDomain(SignupRequestDto source);
}
