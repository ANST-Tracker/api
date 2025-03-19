package com.anst.sd.api.adapter.rest.filter.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.filter.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class FilterDomainMapperTest extends AbstractUnitTest {
    private FilterDomainMapper filterDomainMapper;

    @BeforeEach
    void setUp() {
        filterDomainMapper = Mappers.getMapper(FilterDomainMapper.class);
    }

    @Test
    void mapToDomain_create() {
        CreateFilterDto createFilterDto = readFromFile("/FilterDomainMapperTest/createDto.json", CreateFilterDto.class);

        Filter domain = filterDomainMapper.mapToDomain(createFilterDto);

        assertEqualsToFile("/FilterDomainMapperTest/createFilter.json", domain);
    }

    @Test
    void mapToDomain_update() {
        UpdateFilterDto updateFilterDto = readFromFile("/FilterDomainMapperTest/updateDto.json", UpdateFilterDto.class);

        Filter domain = filterDomainMapper.mapToDomain(updateFilterDto);

        assertEqualsToFile("/FilterDomainMapperTest/updateFilter.json", domain);
    }
}