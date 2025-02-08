package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.adapter.rest.task.write.dto.CreateAbstractTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateAbstractTaskDto;
import com.anst.sd.api.domain.task.*;
import org.mapstruct.*;

import static com.anst.sd.api.domain.task.TaskType.*;

@Mapper(componentModel = "spring")
public interface AbstractTaskDomainMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "simpleId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "type", constant = "EPIC")
    EpicTask toEpicTask(CreateAbstractTaskDto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "simpleId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "type", constant = "EPIC")
    EpicTask toEpicTask(UpdateAbstractTaskDto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "simpleId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "type", constant = "STORY")
    StoryTask toStoryTask(CreateAbstractTaskDto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "simpleId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "type", constant = "STORY")
    StoryTask toStoryTask(UpdateAbstractTaskDto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "simpleId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "type", constant = "SUBTASK")
    Subtask toSubtask(CreateAbstractTaskDto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "simpleId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "type", constant = "SUBTASK")
    Subtask toSubtask(UpdateAbstractTaskDto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "simpleId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "type", constant = "DEFECT")
    DefectTask toDefectTask(CreateAbstractTaskDto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "simpleId", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "type", constant = "DEFECT")
    DefectTask toDefectTask(UpdateAbstractTaskDto source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCommonFields(UpdateAbstractTaskDto dto, @MappingTarget AbstractTask entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEpicFields(UpdateAbstractTaskDto dto, @MappingTarget EpicTask entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDefectFields(UpdateAbstractTaskDto dto, @MappingTarget DefectTask entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStoryFields(UpdateAbstractTaskDto dto, @MappingTarget StoryTask entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSubtaskFields(UpdateAbstractTaskDto dto, @MappingTarget Subtask entity);

    default void updateTaskFromDto(UpdateAbstractTaskDto dto, @MappingTarget AbstractTask entity) {
        updateCommonFields(dto, entity);
        if (entity instanceof EpicTask) {
            updateEpicFields(dto, (EpicTask) entity);
        } else if (entity instanceof StoryTask) {
            updateStoryFields(dto, (StoryTask) entity);
        } else if (entity instanceof Subtask) {
            updateSubtaskFields(dto, (Subtask) entity);
        } else if (entity instanceof DefectTask) {
            updateDefectFields(dto, (DefectTask) entity);
        } else {
            throw new IllegalStateException("Неизвестный тип задачи: " + entity.getClass());
        }
    }

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