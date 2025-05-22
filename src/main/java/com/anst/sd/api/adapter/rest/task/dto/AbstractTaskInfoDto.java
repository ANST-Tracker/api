package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.task.SimpleDictionary;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractTaskInfoDto {
    UUID id;
    String simpleId;
    SprintDto sprint;
    String name;
    String description;
    SimpleDictionary type;
    SimpleDictionary priority;
    Integer storyPoints;
    UserInfoDto assignee;
    UserInfoDto reviewer;
    UserInfoDto creator;
    LocalDate dueDate;
    TimeEstimationDto timeEstimation;
    TimeEstimationDto timeUsed;
    TimeEstimationDto timeRemaining;
    List<TagDto> tags;
    SimpleDictionary status;
}
