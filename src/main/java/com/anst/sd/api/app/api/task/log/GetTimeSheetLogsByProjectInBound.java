package com.anst.sd.api.app.api.task.log;

import com.anst.sd.api.domain.task.Log;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GetTimeSheetLogsByProjectInBound {
    List<Log> get(UUID projectId, UUID userId, LocalDate start, LocalDate end);
}
