package com.anst.sd.api.adapter.rest.task;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.rest.task.dto.AbstractTaskDomainMapper;
import com.anst.sd.api.adapter.rest.task.write.dto.CreateAbstractTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateAbstractTaskDto;
import com.anst.sd.api.domain.task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractTaskDomainMapperTest extends AbstractUnitTest {
    private AbstractTaskDomainMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AbstractTaskDomainMapper.class);
    }

    @Test
    void toEpicTask_fromCreateDto() {
        CreateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/createEpicTaskDto.json", CreateAbstractTaskDto.class);

        EpicTask epicTask = mapper.toEpicTask(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/epicTask.json", epicTask);
        assertEquals(TaskType.EPIC, epicTask.getType());
        assertEquals(TaskStatus.OPEN, epicTask.getStatus());
    }

    @Test
    void toEpicTask_fromUpdateDto() {
        UpdateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/updateEpicTaskDto.json", UpdateAbstractTaskDto.class);

        EpicTask epicTask = mapper.toEpicTask(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/updatedEpicTask.json", epicTask);
        assertEquals(TaskType.EPIC, epicTask.getType());
    }

    @Test
    void toStoryTask_fromCreateDto() {
        CreateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/createStoryTaskDto.json", CreateAbstractTaskDto.class);

        StoryTask storyTask = mapper.toStoryTask(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/storyTask.json", storyTask);
        assertEquals(TaskType.STORY, storyTask.getType());
        assertEquals(TaskStatus.OPEN, storyTask.getStatus());
    }

    @Test
    void toStoryTask_fromUpdateDto() {
        UpdateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/updateStoryTaskDto.json", UpdateAbstractTaskDto.class);

        StoryTask storyTask = mapper.toStoryTask(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/updatedStoryTask.json", storyTask);
        assertEquals(TaskType.STORY, storyTask.getType());
    }

    @Test
    void toSubtask_fromCreateDto() {
        CreateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/createSubtaskDto.json", CreateAbstractTaskDto.class);

        Subtask subtask = mapper.toSubtask(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/subtask.json", subtask);
        assertEquals(TaskType.SUBTASK, subtask.getType());
        assertEquals(TaskStatus.OPEN, subtask.getStatus());
    }

    @Test
    void toSubtask_fromUpdateDto() {
        UpdateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/updateSubtaskDto.json", UpdateAbstractTaskDto.class);

        Subtask subtask = mapper.toSubtask(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/updatedSubtask.json", subtask);
        assertEquals(TaskType.SUBTASK, subtask.getType());
    }

    @Test
    void toDefectTask_fromCreateDto() {
        CreateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/createDefectTaskDto.json", CreateAbstractTaskDto.class);

        DefectTask defectTask = mapper.toDefectTask(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/defectTask.json", defectTask);
        assertEquals(TaskType.DEFECT, defectTask.getType());
        assertEquals(TaskStatus.OPEN, defectTask.getStatus());
    }

    @Test
    void toDefectTask_fromUpdateDto() {
        UpdateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/updateDefectTaskDto.json", UpdateAbstractTaskDto.class);

        DefectTask defectTask = mapper.toDefectTask(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/updatedDefectTask.json", defectTask);
        assertEquals(TaskType.DEFECT, defectTask.getType());
    }

    @Test
    void mapToDomain_fromCreateDto_epic() {
        CreateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/createEpicTaskDto.json", CreateAbstractTaskDto.class);
        dto.setType(TaskType.EPIC);

        AbstractTask task = mapper.mapToDomain(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/epicTask.json", task);
        assertEquals(EpicTask.class, task.getClass());
    }

    @Test
    void mapToDomain_fromCreateDto_story() {
        CreateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/createStoryTaskDto.json", CreateAbstractTaskDto.class);
        dto.setType(TaskType.STORY);

        AbstractTask task = mapper.mapToDomain(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/storyTask.json", task);
        assertEquals(StoryTask.class, task.getClass());
    }

    @Test
    void mapToDomain_fromUpdateDto_subtask() {
        UpdateAbstractTaskDto dto = readFromFile("/AbstractTaskDomainMapperTest/updateSubtaskDto.json", UpdateAbstractTaskDto.class);
        dto.setType(TaskType.SUBTASK);

        AbstractTask task = mapper.mapToDomain(dto);

        assertEqualsToFile("/AbstractTaskDomainMapperTest/updatedSubtask.json", task);
        assertEquals(Subtask.class, task.getClass());
    }
}