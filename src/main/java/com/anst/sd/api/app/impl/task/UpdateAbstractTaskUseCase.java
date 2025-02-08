package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.UpdateAbstractTaskInBound;
import com.anst.sd.api.domain.task.AbstractTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateAbstractTaskUseCase implements UpdateAbstractTaskInBound {
    private final AbstractTaskRepository abstractTaskRepository;

    @Override
    public AbstractTask update(UUID userId, UUID taskId, AbstractTask updatedDto) {
        AbstractTask original = abstractTaskRepository.findById(taskId);
        original.updateFrom(updatedDto, userId);
        return abstractTaskRepository.save(original);
    }
}
