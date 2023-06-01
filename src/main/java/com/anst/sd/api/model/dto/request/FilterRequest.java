package com.anst.sd.api.model.dto.request;

import com.anst.sd.api.model.enums.SortOrder;
import com.anst.sd.api.model.enums.TaskStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterRequest {
    List<SortOrder> orders;
    List<TaskStatus> necessaryStatuses;
    Integer page;
}
