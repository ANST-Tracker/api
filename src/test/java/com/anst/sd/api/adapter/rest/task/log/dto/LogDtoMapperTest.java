package com.anst.sd.api.adapter.rest.task.log.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.TimeEstimation;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.Log;
import com.anst.sd.api.domain.task.Subtask;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogDtoMapperTest extends AbstractUnitTest {
    private LogDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(LogDtoMapper.class);
    }

    @Test
    void mapToDto() {
        User user = new User();
        user
                .setFirstName("firstName")
                .setLastName("lastName")
                .setId(UUID.randomUUID());
        Project project = new Project();
        project
                .setName("projectName")
                .setId(UUID.randomUUID());
        AbstractTask task = new Subtask();
        task.setProject(project);
        task.setSimpleId("PN-3");
        task.setId(UUID.randomUUID());

        Log log = new Log();
        log
                .setUser(user)
                .setTask(task)
                .setComment("content")
                .setTimeEstimation((new TimeEstimation()).setTimeUnit(TimeUnit.HOURS).setAmount(3))
                .setCreated(Instant.now().minusSeconds(10))
                .setId(UUID.randomUUID());

        TimeSheetDto dto = mapper.mapToDto(log);

        assertEquals(log.getId(), dto.getId());
        assertEquals(log.getComment(), dto.getComment());
        assertEquals(log.getCreated(), dto.getCreated());
        assertEquals(log.getTask().getProject().getName(), dto.getProjectName());
        assertEquals(log.getTask().getProject().getId(), dto.getProjectId());
        assertEquals(log.getTask().getSimpleId(), dto.getTaskSimpleId());
        assertEquals(log.getTimeEstimation().getAmount(), dto.getTimeEstimation().getAmount());
        assertEquals(log.getTimeEstimation().getTimeUnit(), dto.getTimeEstimation().getTimeUnit());
    }
}