package com.anst.sd.api.adapter.rest.task.read.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.task.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TasksByDateDtoMapperTest extends AbstractUnitTest {
    private TasksByDateDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TasksByDateDtoMapper.class);
    }

    @Test
    void mapToDto() throws IOException {
        Map<LocalDate, List<Task>> source = objectMapper.readValue(
                new File("src/test/resources/TaskByDateDtoMapperTest/tasksByDate.json"),
                new TypeReference<>() {}
        );
        List<TasksByDateDto> dto = mapper.mapToDto(source);

        assertEqualsToFile("/TaskByDateDtoMapperTest/tasksByDateDto.json", dto);
    }
}
