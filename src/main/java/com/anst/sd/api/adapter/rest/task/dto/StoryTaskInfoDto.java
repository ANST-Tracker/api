package com.anst.sd.api.adapter.rest.task.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryTaskInfoDto extends AbstractTaskInfoDto {
    SprintDto sprint;
    UserInfoDto tester;
    List<TaskPreviewDto> defects;
    TaskPreviewDto epicTask;
}
