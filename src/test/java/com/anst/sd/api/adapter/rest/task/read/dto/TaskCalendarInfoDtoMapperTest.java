package com.anst.sd.api.adapter.rest.task.read.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class TaskCalendarInfoDtoMapperTest  extends AbstractUnitTest {
    private TaskCalendarInfoDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TaskCalendarInfoDtoMapper.class);
    }
    @Test
    void mapToDto() {
        Task source = readFromFile("/TaskCalendarInfoDtoMapperTest/task.json", Task.class);

        TaskCalendarInfoDto dto = mapper.mapToDto(source);

        assertEqualsToFile("/TaskCalendarInfoDtoMapperTest/taskCalendarInfoDto.json", dto);
    }
}
