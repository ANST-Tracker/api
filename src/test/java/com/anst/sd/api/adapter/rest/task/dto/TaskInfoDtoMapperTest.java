package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.task.DefectTask;
import com.anst.sd.api.domain.task.EpicTask;
import com.anst.sd.api.domain.task.StoryTask;
import com.anst.sd.api.domain.task.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class TaskInfoDtoMapperTest extends AbstractUnitTest {
    private TaskInfoDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TaskInfoDtoMapper.class);
    }

    @Test
    void mapToDto_epicTaskInfoDto() {
        EpicTask task = readFromFile("/TaskInfoDtoMapperTest/epicTask.json", EpicTask.class);

        Object dto = mapper.mapToDtoAuto(task);

        assertEqualsToFile("/TaskInfoDtoMapperTest/epicTaskDto.json", dto);
    }

    @Test
    void mapToDto_subtask() {
        Subtask subtask = readFromFile("/TaskInfoDtoMapperTest/subtask.json", Subtask.class);

        Object dto = mapper.mapToDtoAuto(subtask);

        assertEqualsToFile("/TaskInfoDtoMapperTest/subtaskDto.json", dto);
    }

    @Test
    void mapToDto_story() {
        StoryTask task = readFromFile("/TaskInfoDtoMapperTest/storyTask.json", StoryTask.class);

        Object dto = mapper.mapToDtoAuto(task);

        assertEqualsToFile("/TaskInfoDtoMapperTest/storyTaskDto.json", dto);
    }

    @Test
    void mapToDto_defect() {
        DefectTask defectTask = readFromFile("/TaskInfoDtoMapperTest/defectTask.json", DefectTask.class);

        Object dto = mapper.mapToDtoAuto(defectTask);

        assertEqualsToFile("/TaskInfoDtoMapperTest/defectTaskDto.json", dto);
    }
}