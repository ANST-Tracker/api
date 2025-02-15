package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.sprint.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SprintJpaRepository extends JpaRepository<Sprint, UUID> {
}
