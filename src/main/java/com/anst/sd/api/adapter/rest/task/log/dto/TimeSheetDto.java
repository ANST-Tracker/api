package com.anst.sd.api.adapter.rest.task.log.dto;

import com.anst.sd.api.domain.TimeEstimation;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class TimeSheetDto {
    LocalDate date;
    TimeEstimation timeEstimation;
    String taskSimpleId;
    String taskName;
    String comment;
    UUID id;
}
