package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.task.AbstractTaskNotFound;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.domain.task.AbstractTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AbstractTaskRepositoryImpl implements AbstractTaskRepository {
    private final AbstractTaskJpaRepository abstractTaskJpaRepository;

    @Override
    public AbstractTask save(AbstractTask abstractTask) {
        return abstractTaskJpaRepository.save(abstractTask);
    }

    @Override
    public BigDecimal findNextOrderNumber(UUID taskId) {
        return abstractTaskJpaRepository.findNextOrderNumber(taskId);
    }

    @Override
    public AbstractTask findBySimpleId(String simpleId) {
        return abstractTaskJpaRepository.findBySimpleId(simpleId)
                .orElseThrow(() -> new AbstractTaskNotFound(simpleId));
    }
}
