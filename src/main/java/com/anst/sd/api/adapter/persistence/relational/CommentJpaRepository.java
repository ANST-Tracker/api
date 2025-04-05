package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.task.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, UUID> {
    @Query("""
        select c from Comment c
        left join c.task t
        left join t.project p
        where p.id = :projectId
        and t.simpleId = :taskId
    """)
    List<Comment> findAllByTaskAndProject(String taskId, UUID projectId);

    @Query("""
        select c from Comment c
        left join c.task t
        left join t.project p
        left join c.author u
        where p.id = :projectId
        and t.simpleId = :taskId
        and c.id = :id
        and u.id = :authorId
    """)
    Optional<Comment> findByIdAndTaskAndProjectAndAuthor(UUID id, String taskId, UUID projectId, UUID authorId);
}
