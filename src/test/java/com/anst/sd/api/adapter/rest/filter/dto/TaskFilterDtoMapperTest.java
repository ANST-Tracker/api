package com.anst.sd.api.adapter.rest.filter.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.filter.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class TaskFilterDtoMapperTest extends AbstractUnitTest {
    private FilterDtoMapper filterDtoMapper;

    @BeforeEach
    void setUp() {
        filterDtoMapper = Mappers.getMapper(FilterDtoMapper.class);
    }

    @Test
    void mapToDto() {
        Filter filter = readFromFile("/FilterDtoMapperTest/filter.json", Filter.class);

        FilterDto dto = filterDtoMapper.mapToDto(filter);

        assertEqualsToFile("/FilterDtoMapperTest/filterDto.json", dto);
    }
}