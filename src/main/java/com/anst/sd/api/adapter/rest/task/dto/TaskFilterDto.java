package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.domain.filter.FilterPayload;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@EqualsAndHashCode
@Data
@Accessors(chain = true)
public class TaskFilterDto {
    @NotNull
    private FilterPayload payload;
    private UUID projectId;
}
