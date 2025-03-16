package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.AbstractTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbstractTaskJpaRepository extends JpaRepository<AbstractTask, UUID> {
    @Query("SELECT COALESCE(MAX(t.orderNumber), 0) FROM AbstractTask t")
    BigDecimal findNextOrderNumber(UUID taskId);

    Optional<AbstractTask> findBySimpleId(String simpleId);

    Optional<AbstractTask> findByIdAndProjectId(UUID taskId, UUID projectId);
}