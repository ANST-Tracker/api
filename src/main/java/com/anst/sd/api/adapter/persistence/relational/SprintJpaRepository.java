package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.sprint.Sprint;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SprintJpaRepository extends JpaRepository<Sprint, UUID> {

    @EntityGraph(attributePaths = {"defects"})
    Optional<Sprint> findWithDefectsByIdAndProjectId(UUID sprintId, UUID projectId);

    @EntityGraph(attributePaths = {"stories"})
    Optional<Sprint> findWithStoriesByIdAndProjectId(UUID sprintId, UUID projectId);

    @Query("""
        select s from Sprint s
        left join s.project p
        where s.project.id = :projectId
        """)
    List<Sprint> findAllByProjectId(UUID projectId);

    default Sprint getByIdAndProjectId(UUID sprintId, UUID projectId) {
        Sprint sprint = findWithStoriesByIdAndProjectId(sprintId, projectId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sprint not found with id =" + sprintId + " and projectId =" + projectId));
        findWithDefectsByIdAndProjectId(sprintId, projectId)
                .map(Sprint::getDefects)
                .ifPresentOrElse(sprint::setDefects, () -> sprint.setDefects(Collections.emptyList())
                );
        return sprint;
    }
}
