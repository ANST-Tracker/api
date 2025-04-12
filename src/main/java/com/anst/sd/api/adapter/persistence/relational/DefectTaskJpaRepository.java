package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.DefectTask;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DefectTaskJpaRepository extends JpaRepository<DefectTask, UUID> {
    @EntityGraph("defect-task-full")
    DefectTask getBySimpleId(String simpleId);
}
