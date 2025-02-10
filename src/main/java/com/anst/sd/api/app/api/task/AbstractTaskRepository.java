package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.AbstractTask;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface AbstractTaskRepository {
    AbstractTask save(AbstractTask abstractTask);
    AbstractTask findById(UUID taskId);

    BigDecimal findNextOrderNumber(UUID taskId);
}
