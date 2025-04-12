package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.task.SubtaskRepository;
import com.anst.sd.api.domain.task.Subtask;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubtaskRepositoryImpl implements SubtaskRepository {
    private final SubtaskJpaRepository subtaskJpaRepository;

    @Override
    public Subtask getBySimpleId(String simpleId) {
        Subtask task = subtaskJpaRepository.getBySimpleId(simpleId);
        Hibernate.initialize(task.getLogs());
        return task;
    }
}
