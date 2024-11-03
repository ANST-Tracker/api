package com.anst.sd.api.adapter.rest.task.read.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class TaskCalendarInfoDto {
    @NotNull
    Long id;
    @JsonProperty("name")
    String data;
}