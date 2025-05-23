package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.sprint.Sprint;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SprintJpaRepository extends JpaRepository<Sprint, UUID> {

    @EntityGraph(attributePaths = {"defects"})
    Sprint findWithDefectsByIdAndProjectId(UUID sprintId, UUID projectId);

    @EntityGraph(attributePaths = {"stories"})
    Sprint findWithStoriesByIdAndProjectId(UUID sprintId, UUID projectId);

    @Query("""
        select s from Sprint s
        left join s.project p
        where s.project.id = :projectId
        """)
    List<Sprint> findAllByProjectId(UUID projectId);

    default Sprint getByIdAndProjectId(UUID sprintId, UUID projectId) {
        Sprint sprintWithStories = findWithStoriesByIdAndProjectId(sprintId, projectId);
        Sprint sprintWithDefects = findWithDefectsByIdAndProjectId(sprintId, projectId);
        sprintWithStories.setDefects(sprintWithDefects.getDefects());
        return sprintWithStories;
    }
}
