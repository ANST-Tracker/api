package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.task.StoryTaskRepository;
import com.anst.sd.api.domain.task.StoryTask;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoryTaskRepositoryImpl implements StoryTaskRepository {
    private final StoryTaskJpaRepository storyTaskJpaRepository;

    @Override
    public StoryTask getBySimpleId(String simpleId) {
        StoryTask task = storyTaskJpaRepository.getBySimpleId(simpleId);
        Hibernate.initialize(task.getLogs());
        Hibernate.initialize(task.getDefects());
        return task;
    }
}
