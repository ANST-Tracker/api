package com.anst.sd.api.app.api.task.log;

import com.anst.sd.api.domain.task.Log;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GetTimeSheetLogsInBound {
    List<Log> get(UUID userId, LocalDate start, LocalDate end);
}
