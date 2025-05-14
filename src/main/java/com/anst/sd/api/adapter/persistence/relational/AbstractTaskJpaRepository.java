package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.TaskType;
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

    Optional<AbstractTask> findBySimpleIdAndProjectId(String simpleId, UUID projectId);

    @Query(value = """
        select t.type from abstract_task t
        left join project p on t.project_id = p.id
        left join users_projects u on p.id = u.project_id
        where t.simple_id = :simpleId
        and (u.user_id = :userId or p.head_id = :userId)
    """, nativeQuery = true)
    Optional<TaskType> findTypeBySimpleIdAndUserId(String simpleId, UUID userId);
}