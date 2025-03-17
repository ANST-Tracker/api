package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.AbstractTask;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface CreateAbstractTaskInBound {
    AbstractTask create(UUID userId, AbstractTask task);
}
