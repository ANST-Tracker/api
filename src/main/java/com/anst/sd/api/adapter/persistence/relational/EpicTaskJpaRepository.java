package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.EpicTask;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EpicTaskJpaRepository extends JpaRepository<EpicTask, UUID> {
    @EntityGraph("epic-task-full")
    EpicTask getBySimpleId(String simpleId);
}
