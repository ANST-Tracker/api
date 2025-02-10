package com.anst.sd.api.adapter.rest.task.write.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAbstractTaskStatusDto {
    @NotBlank
    private String status;
}
