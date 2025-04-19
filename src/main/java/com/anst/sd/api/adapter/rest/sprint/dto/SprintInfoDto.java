package com.anst.sd.api.adapter.rest.sprint.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class SprintInfoDto {
    UUID id;
    String name;
    String description;
    LocalDate startDate;
    LocalDate endDate;
    Boolean isActive;
    UUID projectId;
}
