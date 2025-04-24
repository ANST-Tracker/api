package com.anst.sd.api.adapter.rest.sprint.read.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSprintDto {
    @NotBlank
    String name;
    @NotNull
    LocalDate startDate;
    LocalDate endDate;
    @NotNull
    Boolean isActive;
    String description;
    @NotNull
    UUID projectId;
}
