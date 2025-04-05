package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.task.EpicTaskRepository;
import com.anst.sd.api.domain.task.EpicTask;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EpicTaskRepositoryImpl implements EpicTaskRepository {
    private final EpicTaskJpaRepository epicTaskJpaRepository;

    @Override
    public EpicTask getBySimpleId(String simpleId) {
        EpicTask task = epicTaskJpaRepository.getBySimpleId(simpleId);
        Hibernate.initialize(task.getLogs());
        Hibernate.initialize(task.getStories());
        return task;
    }
}
