package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.adapter.rest.task.write.dto.CreateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskDto;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public abstract class TaskDomainMapperDecorator implements TaskDomainMapper {
    @Autowired
    @Qualifier("delegate")
    private TaskDomainMapper delegate;

    @Override
    public Task mapToDomain(CreateTaskDto source) {
        Task task = delegate.mapToDomain(source);
        setTagsFromIds(task, source.getTagIds());
        return task;
    }

    @Override
    public Task mapToDomain(UpdateTaskDto source) {
        Task task = delegate.mapToDomain(source);
        setTagsFromIds(task, source.getTagIds());
        return task;
    }

    private void setTagsFromIds(Task task, List<Long> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> tags = tagIds.stream()
                    .map(id -> {
                        Tag tag = new Tag();
                        tag.setId(id);
                        return tag;
                    })
                    .toList();
            task.setTags(tags);
        }
    }
}
