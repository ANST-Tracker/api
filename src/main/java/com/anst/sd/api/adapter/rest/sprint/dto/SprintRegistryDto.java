package com.anst.sd.api.adapter.rest.sprint.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SprintRegistryDto {
    UUID id;
    String name;
    LocalDate startDate;
    LocalDate endDate;
    Boolean isActive;
    String description;
    UUID projectId;
    int storyCount;
    int defectCount;
    List<StoryTaskDto> stories;
    List<DefectTaskDto> defects;
}
