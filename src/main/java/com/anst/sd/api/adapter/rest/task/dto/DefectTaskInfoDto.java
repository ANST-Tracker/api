package com.anst.sd.api.adapter.rest.task.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefectTaskInfoDto extends AbstractTaskInfoDto {
    UUID id;
    SprintDto sprint;
    UserInfoDto tester;
    TaskPreviewDto storyTask;
}
