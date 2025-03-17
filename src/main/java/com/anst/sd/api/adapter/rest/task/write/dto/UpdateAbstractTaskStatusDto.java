package com.anst.sd.api.adapter.rest.task.write.dto;

import com.anst.sd.api.domain.task.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAbstractTaskStatusDto {
    @NotNull
    private TaskStatus status;
}
