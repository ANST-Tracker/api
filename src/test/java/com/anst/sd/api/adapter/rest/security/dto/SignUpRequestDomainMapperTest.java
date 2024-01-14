package com.anst.sd.api.adapter.rest.security.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;
import org.junit.jupiter.api.Test;
class SignUpRequestDomainMapperTest extends AbstractUnitTest {
    private SignupRequestDomainMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(SignupRequestDomainMapper.class);
    }

    @Test
    void mapToDomain() {
        SignupRequestDto source = readFromFile("/SignUpRequestDomainMapperTest/signUpRequest.json", SignupRequestDto.class);

        User domain = mapper.mapToDomain(source);

        assertEqualsToFile("/SignUpRequestDomainMapperTest/signUpRequestDomain.json", domain);

    }
}
