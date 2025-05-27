package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.adapter.rest.task.write.dto.CreateAbstractTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateAbstractTaskDto;
import com.anst.sd.api.domain.task.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AbstractTaskDomainMapper {
    @Mapping(target = "type", constant = "EPIC")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "reviewerId", target = "reviewer.id")
    EpicTask toEpicTask(CreateAbstractTaskDto source);

    @Mapping(target = "type", constant = "EPIC")
    EpicTask toEpicTask(UpdateAbstractTaskDto source);

    @Mapping(target = "type", constant = "STORY")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "reviewerId", target = "reviewer.id")
    @Mapping(source = "epicTaskId", target = "epicTask.id")
    @Mapping(source = "sprintId", target = "sprint.id")
    @Mapping(source = "testerId", target = "tester.id")
    StoryTask toStoryTask(CreateAbstractTaskDto source);

    @Mapping(target = "type", constant = "STORY")
    @Mapping(source = "testerId", target = "tester.id")
    @Mapping(source = "epicTaskId", target = "epicTask.id")
    StoryTask toStoryTask(UpdateAbstractTaskDto source);

    @Mapping(target = "type", constant = "SUBTASK")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "reviewerId", target = "reviewer.id")
    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "storyTaskId", target = "storyTask.id")
    Subtask toSubtask(CreateAbstractTaskDto source);

    @Mapping(target = "type", constant = "SUBTASK")
    @Mapping(target = "storyTask.id", source = "storyTaskId")
    Subtask toSubtask(UpdateAbstractTaskDto source);

    @Mapping(target = "type", constant = "DEFECT")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "reviewerId", target = "reviewer.id")
    @Mapping(source = "storyTaskId", target = "storyTask.id")
    @Mapping(source = "sprintId", target = "sprint.id")
    DefectTask toDefectTask(CreateAbstractTaskDto source);

    @Mapping(target = "type", constant = "DEFECT")
    DefectTask toDefectTask(UpdateAbstractTaskDto source);

    @Named("mapToDomain")
    default AbstractTask mapToDomain(CreateAbstractTaskDto source) {
        return switch (source.getType()) {
            case EPIC -> toEpicTask(source);
            case STORY -> toStoryTask(source);
            case SUBTASK -> toSubtask(source);
            case DEFECT -> toDefectTask(source);
        };
    }

    @Named("mapToDomain")
    default AbstractTask mapToDomain(UpdateAbstractTaskDto source) {
        return switch (source.getType()) {
            case EPIC -> toEpicTask(source);
            case STORY -> toStoryTask(source);
            case SUBTASK -> toSubtask(source);
            case DEFECT -> toDefectTask(source);
        };
    }
}