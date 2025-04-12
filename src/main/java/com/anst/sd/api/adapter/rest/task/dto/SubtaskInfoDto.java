package com.anst.sd.api.adapter.rest.task.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubtaskInfoDto extends AbstractTaskInfoDto {
    TaskPreviewDto storyTask;
}
