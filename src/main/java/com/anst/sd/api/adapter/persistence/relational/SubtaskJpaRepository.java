package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubtaskJpaRepository extends JpaRepository<Subtask, UUID> {
}
