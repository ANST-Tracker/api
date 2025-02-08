package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.AbstractTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AbstractTaskJpaRepository extends JpaRepository<AbstractTask, UUID> {

    @Query("SELECT COALESCE(MAX(t.simpleId), 0) + 1 FROM AbstractTask t WHERE t.id = :taskId")
    Integer findNextSimpleIdByTask(@Param("taskId") UUID taskId);
}
