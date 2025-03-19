package com.anst.sd.api.adapter.rest.filter.dto;

import com.anst.sd.api.adapter.rest.dto.AbstractFilterDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class FilterDto extends AbstractFilterDto {
    private String id;
    private UUID projectId;
}
