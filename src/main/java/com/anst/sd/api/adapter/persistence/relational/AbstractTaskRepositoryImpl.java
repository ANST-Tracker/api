package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.task.AbstractTaskNotFound;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.domain.filter.Filter;
import com.anst.sd.api.domain.task.AbstractTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AbstractTaskRepositoryImpl implements AbstractTaskRepository {
    private final AbstractTaskJpaRepository abstractTaskJpaRepository;
    private final TaskFindByFilterDelegate delegate;

    @Override
    public AbstractTask save(AbstractTask abstractTask) {
        return abstractTaskJpaRepository.save(abstractTask);
    }

    @Override
    public BigDecimal findNextOrderNumber(UUID taskId) {
        return abstractTaskJpaRepository.findNextOrderNumber(taskId);
    }

    @Override
    public AbstractTask getBySimpleId(String simpleId) {
        return abstractTaskJpaRepository.findBySimpleId(simpleId)
                .orElseThrow(() -> new AbstractTaskNotFound(simpleId));
    }

    @Override
    public AbstractTask getByIdAndProjectId(UUID uuid, UUID projectId) {
        return abstractTaskJpaRepository.findByIdAndProjectId(uuid, projectId)
                .orElseThrow(() -> new AbstractTaskNotFound(uuid));
    }

    @Override
    public List<AbstractTask> findByFilter(Filter filter) {
        List<UUID> taskIds = delegate.findByFilter(filter);
        return abstractTaskJpaRepository.findAllById(taskIds);
    }

    @Override
    public AbstractTask getByIdAndProjectId(String simpleId, UUID projectId) {
        return abstractTaskJpaRepository.findByIdAndProjectId(simpleId, projectId)
                .orElseThrow(() -> new AbstractTaskNotFound(simpleId));
    }

    @Override
    public boolean existsBySimpleIdAndProjectId(String simpleId, UUID projectId) {
        return abstractTaskJpaRepository.existsBySimpleIdAndProjectId(simpleId, projectId);
    }
}
