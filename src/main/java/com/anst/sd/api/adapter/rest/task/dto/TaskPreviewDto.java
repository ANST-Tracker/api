package com.anst.sd.api.adapter.rest.task.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskPreviewDto {
    String simpleId;
    String name;
}
