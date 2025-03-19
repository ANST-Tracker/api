package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.filter.Filter;
import com.anst.sd.api.domain.filter.FilterPayload;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.Project_;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.tag.Tag_;
import com.anst.sd.api.domain.task.*;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.domain.user.User_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskFindByFilterDelegate {
    private final EntityManager entityManager;

    public List<UUID> findByFilter(Filter filter) {
        try (Session session = entityManager.unwrap(Session.class)) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UUID> criteriaQuery = criteriaBuilder.createQuery(UUID.class);
            Root<AbstractTask> task = criteriaQuery.from(AbstractTask.class);

            TaskJoinPredicates taskJoinPredicates = getJoinPredicates(task);

            List<Predicate> predicateList = new ArrayList<>(generateTaskPredicates(
                criteriaBuilder, filter, task, taskJoinPredicates));
            return createQueryAndGetResults(session, criteriaQuery, predicateList, task);
        }
    }

    private List<Predicate> generateTaskPredicates(CriteriaBuilder criteriaBuilder,
        Filter filter,
        Root<AbstractTask> taskRoot,
        TaskJoinPredicates taskJoinPredicates) {
        List<Predicate> predicates = new ArrayList<>();
        addSimplePredicates(criteriaBuilder, filter, taskRoot, predicates);
        addProjectPredicates(criteriaBuilder, filter, predicates, taskJoinPredicates);
        addUsersPredicates(filter, predicates, taskJoinPredicates);
        addTagsPredicates(criteriaBuilder, filter, taskRoot, predicates, taskJoinPredicates);
        return predicates;
    }

    private void addProjectPredicates(CriteriaBuilder criteriaBuilder,
        Filter filter,
        List<Predicate> predicates,
        TaskJoinPredicates taskJoinPredicates) {
        if (filter.getProjectId() != null) {
            predicates.add(criteriaBuilder.equal(taskJoinPredicates.getProjectJoin().get(Project_.id), filter.getProjectId()));
        }
    }

    private void addUsersPredicates(Filter filter, List<Predicate> predicates,
        TaskJoinPredicates taskJoinPredicates) {
        FilterPayload payload = filter.getPayload();
        if (!CollectionUtils.isEmpty(payload.getAssigneeIds())) {
            predicates.add(taskJoinPredicates.getAssigneeJoin().get(User_.id).in(payload.getAssigneeIds()));
        }
        if (!CollectionUtils.isEmpty(payload.getReviewerIds())) {
            predicates.add(taskJoinPredicates.getReviewerJoin().get(User_.id).in(payload.getReviewerIds()));
        }
        if (!CollectionUtils.isEmpty(payload.getCreatorIds())) {
            predicates.add(taskJoinPredicates.getCreatorJoin().get(User_.id).in(payload.getCreatorIds()));
        }
    }

    private void addSimplePredicates(CriteriaBuilder criteriaBuilder, Filter filter, Root<AbstractTask> taskRoot,
        List<Predicate> predicates) {
        FilterPayload payload = filter.getPayload();
        if (payload.getNameFragment() != null) {
            predicates.add(criteriaBuilder.like(taskRoot.get(AbstractTask_.name), filter.getPayload().getNameFragment()));
        }
        if (!CollectionUtils.isEmpty(payload.getTypes())) {
            predicates.add(taskRoot.get(AbstractTask_.type).in(payload.getTypes()));
        }
        if (!CollectionUtils.isEmpty(payload.getPriorities())) {
            predicates.add(taskRoot.get(AbstractTask_.priority).in(payload.getPriorities()));
        }
        if (!CollectionUtils.isEmpty(payload.getStoryPoints())) {
            predicates.add(taskRoot.get(AbstractTask_.storyPoints).in(payload.getStoryPoints()));
        }
        if (!CollectionUtils.isEmpty(payload.getStatuses())) {
            predicates.add(taskRoot.get(AbstractTask_.status).in(payload.getStatuses()));
        }
    }

    private void addTagsPredicates(CriteriaBuilder criteriaBuilder, Filter filter, Root<AbstractTask> taskRoot, List<Predicate> predicates,
        TaskJoinPredicates taskJoinPredicates) {
        if (!CollectionUtils.isEmpty(filter.getPayload().getTagIds())) {
            Join<AbstractTask, Tag> tagJoin = taskJoinPredicates.getTagJoin();
            if (tagJoin == null) {
                tagJoin = taskRoot.join(AbstractTask_.tags, JoinType.LEFT);
                taskJoinPredicates.setTagJoin(tagJoin);
            }

            Join<AbstractTask, Tag> finalTagJoin = tagJoin;
            List<Predicate> tagPredicates = filter.getPayload().getTagIds().stream()
                .map(tagId -> criteriaBuilder.equal(finalTagJoin.get(Tag_.id), tagId))
                .toList();

            predicates.add(criteriaBuilder.or(tagPredicates.toArray(new Predicate[0])));
        }
    }

    private List<UUID> createQueryAndGetResults(Session session,
        CriteriaQuery<UUID> criteriaQuery,
        List<Predicate> predicateList,
        Root<AbstractTask> taskRoot) {
        criteriaQuery
            .select(taskRoot.get(AbstractTask_.id))
            .where(predicateList.toArray(new Predicate[0]));
        TypedQuery<UUID> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private TaskJoinPredicates getJoinPredicates(Root<AbstractTask> taskRoot) {
        TaskJoinPredicates taskJoinPredicates = new TaskJoinPredicates();
        taskJoinPredicates.setProjectJoin(taskRoot.join(AbstractTask_.project, JoinType.LEFT));
        taskJoinPredicates.setAssigneeJoin(taskRoot.join(AbstractTask_.assignee, JoinType.LEFT));
        taskJoinPredicates.setCreatorJoin(taskRoot.join(AbstractTask_.creator, JoinType.LEFT));
        taskJoinPredicates.setReviewerJoin(taskRoot.join(AbstractTask_.reviewer, JoinType.LEFT));
        taskJoinPredicates.setTagJoin(taskRoot.join(AbstractTask_.tags, JoinType.LEFT));
        return taskJoinPredicates;
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    private static class TaskJoinPredicates {
        private Join<AbstractTask, Project> projectJoin;
        private Join<AbstractTask, Tag> tagJoin;
        private Join<AbstractTask, User> creatorJoin;
        private Join<AbstractTask, User> reviewerJoin;
        private Join<AbstractTask, User> assigneeJoin;
        private Join<AbstractTask, DefectTask> defectJoin;
        private Join<AbstractTask, StoryTask> storyJoin;
        private Join<AbstractTask, EpicTask> epicJoin;
        private Join<AbstractTask, Subtask> subtaskJoin;
    }
}