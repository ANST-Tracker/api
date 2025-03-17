package com.anst.sd.api.adapter.rest.tag.write.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.tag.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class TagDomainMapperTest extends AbstractUnitTest {
    private TagDomainMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TagDomainMapper.class);
    }

    @Test
    void mapToDomain__createTaskDto() {
        CreateTagDto source = readFromFile("/TagDomainMapperTest/tag.json", CreateTagDto.class);

        Tag domain = mapper.mapToDomain(source);

        assertEqualsToFile("/TagDomainMapperTest/tagDomain.json", domain);
    }
}
