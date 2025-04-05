package com.anst.sd.api.adapter.rest.tag.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.tag.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

public class TagInfoDtoMapperTest extends AbstractUnitTest {
    private TagDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TagDtoMapper.class);
    }

    @Test
    void mapToDto() {
        Tag tag = readFromFile("/TagInfoDtoMapperTest/tag.json", Tag.class);

        TagInfoDto dto = mapper.mapToDto(tag);

        assertEqualsToFile("/TagInfoDtoMapperTest/tagInfoDto.json", dto);
    }

    @Test
    void mapToDtoList() {
        List<Tag> list = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            list.add(readFromFile("/TagInfoDtoMapperTest/tag.json", Tag.class));
        }

        List<TagInfoDto> dto = mapper.mapToDto(list);

        assertEqualsToFile("/TagInfoDtoMapperTest/listTagInfoDto.json", dto);
    }
}
