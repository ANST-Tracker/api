package com.anst.sd.api.adapter.rest.task.log.dto;

import com.anst.sd.api.domain.TimeEstimation;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class CreateUpdateLogDto {
    String comment;
    TimeEstimation timeEstimation;
}
