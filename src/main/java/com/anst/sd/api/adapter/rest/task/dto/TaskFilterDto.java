package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.adapter.rest.dto.AbstractFilterDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskFilterDto extends AbstractFilterDto {
    private UUID projectId;
}
