package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.AbstractTask;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AbstractTaskRepository {
    AbstractTask save(AbstractTask abstractTask);
    AbstractTask findById(UUID taskId);
    Integer findNextSimpleIdByProject(UUID taskId);
}
