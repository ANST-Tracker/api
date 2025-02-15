package com.anst.sd.api.adapter.rest.task.write.dto;

import com.anst.sd.api.domain.task.FullCycleStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAbstractTaskStatusDto {
    @NotNull
    private FullCycleStatus status;
}
