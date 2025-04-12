package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.StoryTask;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoryTaskJpaRepository extends JpaRepository<StoryTask, UUID> {
    @EntityGraph("story-task-full")
    StoryTask getBySimpleId(String simpleId);
}
