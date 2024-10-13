package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.adapter.rest.task.write.dto.CreateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskDto;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.task.Task;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskDomainMapper {
    @Mapping(target = "tags", ignore = true)
    Task mapToDomain(CreateTaskDto source);

    @Mapping(target = "tags", ignore = true)
    Task mapToDomain(UpdateTaskDto source);

    @AfterMapping
    default void createTags(CreateTaskDto source, @MappingTarget Task task) {
        if (source.getTagIds() == null || source.getTagIds().isEmpty()) {
            return;
        }

        List<Tag> tags = source.getTagIds().stream()
                .map(id -> {
                    Tag tag = new Tag();
                    tag.setId(id);
                    return tag;
                })
                .toList();

        task.setTags(tags);
    }

    @AfterMapping
    default void updateTags(UpdateTaskDto source, @MappingTarget Task task) {
        if (source.getTagIds() == null || source.getTagIds().isEmpty()) {
            return;
        }

        List<Tag> tags = source.getTagIds().stream()
                .map(id -> {
                    Tag tag = new Tag();
                    tag.setId(id);
                    return tag;
                })
                .toList();

        task.setTags(tags);
    }
}
