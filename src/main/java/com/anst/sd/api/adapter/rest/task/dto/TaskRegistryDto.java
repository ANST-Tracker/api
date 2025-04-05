package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.SimpleDictionary;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRegistryDto {
    String simpleId;
    String name;
    SimpleDictionary type;
    SimpleDictionary status;
    SimpleDictionary priority;
    String assigneeFullName;
    String creatorFullName;
}
