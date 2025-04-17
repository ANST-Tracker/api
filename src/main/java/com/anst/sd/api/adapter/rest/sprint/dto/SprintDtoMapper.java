package com.anst.sd.api.adapter.rest.sprint.dto;

import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.task.DefectTask;
import com.anst.sd.api.domain.task.StoryTask;
import com.anst.sd.api.domain.task.Subtask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SprintDtoMapper {
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "storyCount", expression = "java(sprint.getStories() == null ? 0 : sprint.getStories().size())")
    @Mapping(target = "defectCount", expression = "java(sprint.getDefects() == null ? 0 : sprint.getDefects().size())")
    SprintRegistryDto mapToDto(Sprint sprint);

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "subtasks", source = "subtasks")
    StoryTaskDto storyTaskToStoryTaskDto(StoryTask storyTask);

    List<StoryTaskDto> storyTaskListToStoryTaskDtoList(List<StoryTask> storyTasks);

    @Mapping(target = "assigneeId", source = "assignee.id")
    DefectTaskDto defectTaskToDefectTaskDto(DefectTask defectTask);

    List<DefectTaskDto> defectTaskListToDefectTaskDtoList(List<DefectTask> defectTasks);

    @Mapping(target = "assigneeId", source = "assignee.id")
    SubtaskDto subtaskToSubtaskDto(Subtask subtask);

    List<SubtaskDto> subtaskListToSubtaskDtoList(List<Subtask> subtasks);
}
