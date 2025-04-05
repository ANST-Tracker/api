package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.task.DefectTaskRepository;
import com.anst.sd.api.domain.task.DefectTask;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefectTaskRepositoryImpl implements DefectTaskRepository {
    private final DefectTaskJpaRepository defectTaskJpaRepository;

    @Override
    public DefectTask getBySimpleId(String simpleId) {
        DefectTask task = defectTaskJpaRepository.getBySimpleId(simpleId);
        Hibernate.initialize(task.getLogs());
        return task;
    }
}
