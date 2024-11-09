package com.anst.sd.api.adapter.rest.task.read.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.task.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TasksByDateDtoMapperTest extends AbstractUnitTest {
    @Autowired
    private TasksByDateDtoMapper mapper;

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
