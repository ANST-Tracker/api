package com.anst.sd.api.adapter.rest.task.log.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.task.Log;
import com.anst.sd.api.domain.task.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogDtoMapperTest extends AbstractUnitTest {
    private LogDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(LogDtoMapper.class);
    }

    @Test
    void mapToDto() {
        Log log = readFromFile("/LogDtoMapperTest/log.json", Log.class);
        log.setTask(readFromFile("/TaskInfoDtoMapperTest/subtask.json", Subtask.class));

        TimeSheetDto dto = mapper.mapToDto(log);

        assertEquals(log.getId(), dto.getId());
        assertEquals(log.getComment(), dto.getComment());
        assertEquals(log.getDate(), dto.getDate());
        assertEquals(log.getTask().getProject().getName(), dto.getProjectName());
        assertEquals(log.getTask().getProject().getId(), dto.getProjectId());
        assertEquals(log.getTask().getSimpleId(), dto.getTaskSimpleId());
        assertEquals(log.getTimeEstimation().getAmount(), dto.getTimeEstimation().getAmount());
        assertEquals(log.getTimeEstimation().getTimeUnit(), dto.getTimeEstimation().getTimeUnit());
    }
}