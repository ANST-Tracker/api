package com.anst.sd.api.adapter.rest.task.read.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.task.TaskFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class FilterRequestDomainMapperTest extends AbstractUnitTest {
    private FilterRequestDomainMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(FilterRequestDomainMapper.class);
    }

    @Test
    void mapToDomain() {
        TaskFilterRequestDto source = readFromFile("/FilterRequestDomainMapperTest/filterRequest.json", TaskFilterRequestDto.class);

        TaskFilter domain = mapper.mapToDomain(source);

        assertEqualsToFile("/FilterRequestDomainMapperTest/filterRequestDomain.json", domain);
    }
}
