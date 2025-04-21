package com.anst.sd.api.app.impl.task.log;

import com.anst.sd.api.app.api.task.log.GetTimeSheetLogsInBound;
import com.anst.sd.api.app.api.task.log.LogRepository;
import com.anst.sd.api.domain.task.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetTimeSheetLogsUseCase implements GetTimeSheetLogsInBound {
    private final LogRepository logRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Log> get(UUID userId, LocalDate start, LocalDate end) {
        log.info("Getting all logs for user {} from {} to {}", userId, start.toString(), end.toString());
        return logRepository.findAllByPeriodAndUser(userId, start, end);
    }
}
