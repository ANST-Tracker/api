package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.domain.task.AbstractTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleIdGenerationDelegate {
    private final AbstractTaskRepository abstractTaskRepository;

    public Integer idGenerator(AbstractTask task) {
        return abstractTaskRepository.findNextSimpleIdByProject(task.getId());
    }
}
