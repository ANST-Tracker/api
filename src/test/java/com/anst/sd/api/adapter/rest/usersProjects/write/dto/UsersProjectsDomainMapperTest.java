package com.anst.sd.api.adapter.rest.usersProjects.write.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.UsersProjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class UsersProjectsDomainMapperTest extends AbstractUnitTest {
    private UsersProjectsDomainMapper mapper;

    @BeforeEach
    void setUp(){
        mapper = Mappers.getMapper(UsersProjectsDomainMapper.class);
    }

    @Test
    void mapToDto() {
        AddUserInProjectDto source = readFromFile("/UsersProjectsDomainMapperTest/userProjectDto.json", AddUserInProjectDto.class);

        UsersProjects domain = mapper.mapToDomain(source);

        assertEqualsToFile("/UsersProjectsDomainMapperTest/userProject.json", domain);
    }
}
