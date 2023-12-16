package com.anst.sd.api.adapter.rest.task.write.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTaskDto {
    @NotNull
    @NotBlank
    String data;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime deadline;
    String description;
}
