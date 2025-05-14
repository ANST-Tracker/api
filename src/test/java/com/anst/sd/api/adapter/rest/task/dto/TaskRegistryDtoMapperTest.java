package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.task.StoryTask;
import com.anst.sd.api.domain.task.TaskPriority;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.task.TaskType;
import com.anst.sd.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskRegistryDtoMapperTest extends AbstractUnitTest {
    private TaskRegistryDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TaskRegistryDtoMapper.class);
    }

    @Test
    void mapToDto() {
        StoryTask storyTask = new StoryTask();
        storyTask
                .setSimpleId("TEST_ID")
                .setName("TEST_NAME")
                .setType(TaskType.STORY)
                .setStatus(TaskStatus.OPEN)
                .setPriority(TaskPriority.MAJOR)
                .setAssignee(new User()
                        .setFirstName("John")
                        .setLastName("Smith"))
                .setCreator(new User()
                        .setFirstName("FirstName")
                        .setLastName("LastName")
                );

        TaskRegistryDto dto = mapper.mapToDto(storyTask);

        assertEquals("TEST_ID", dto.getSimpleId());
        assertEquals("TEST_NAME", dto.getName());
        assertEquals(TaskType.STORY.name(), dto.getType().getCode());
        assertEquals(TaskType.STORY.getValue(), dto.getType().getValue());
        assertEquals(TaskStatus.OPEN.name(), dto.getStatus().getCode());
        assertEquals(TaskStatus.OPEN.getValue(), dto.getStatus().getValue());
        assertEquals(TaskPriority.MAJOR.name(), dto.getPriority().getCode());
        assertEquals(TaskPriority.MAJOR.getValue(), dto.getPriority().getValue());
        assertEquals("LastName FirstName", dto.getCreatorFullName());
        assertEquals("Smith John", dto.getAssigneeFullName());
    }
}