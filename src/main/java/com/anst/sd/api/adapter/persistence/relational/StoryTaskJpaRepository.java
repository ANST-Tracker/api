package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.StoryTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoryTaskJpaRepository extends JpaRepository<StoryTask, UUID> {
}
