package com.anst.sd.api.adapter.rest.task.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.TimeUnit;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class TimeEstimationDto {
    TimeUnit timeUnit;
    Double amount;
}
