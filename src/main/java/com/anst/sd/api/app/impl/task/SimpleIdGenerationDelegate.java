package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.domain.task.AbstractTask;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleIdGenerationDelegate {
    public static String idGenerator(AbstractTask task) {
        String taskKey = task.getProject().getKey();
        Integer nextTaskId = task.getProject().getNextTaskId();
        return taskKey + "-" + nextTaskId;
    }
}
