package com.anst.sd.api.adapter.rest.dto;

import com.anst.sd.api.domain.filter.FilterPayload;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class AbstractFilterDto {
    @NotNull
    private FilterPayload payload;
    @NotNull
    private String name;
}
