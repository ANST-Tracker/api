package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.DefectTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DefectJpaRepository extends JpaRepository<DefectTask, UUID> {
}
