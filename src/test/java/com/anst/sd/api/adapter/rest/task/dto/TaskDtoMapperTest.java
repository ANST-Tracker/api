package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class TaskDtoMapperTest extends AbstractUnitTest {
    private TaskDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TaskDtoMapper.class);
    }

    @Test
    void mapToDto() {
        Task source = readFromFile("/TaskDtoMapperTest/task.json", Task.class);

        TaskInfoDto dto = mapper.mapToDto(source);

        assertEqualsToFile("/TaskDtoMapperTest/taskDto.json", dto);
    }
}
