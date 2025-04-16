package com.anst.sd.api.app.api.task.log;

import com.anst.sd.api.domain.task.Log;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GetLogsForTimeSheetInBound {
    List<Log> get(UUID userId, LocalDateTime start, LocalDateTime end);
}
