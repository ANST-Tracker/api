package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.DateRangeFilter;
import com.anst.sd.api.app.api.task.TaskFilter;
import com.anst.sd.api.app.api.task.TaskNotFoundException;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.Project_;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.task.Task_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {
    private final TaskJpaRepository taskJpaRepository;
    private final EntityManager entityManager;
    @Value("${pageable.size}")
    private Integer pageSize;

    @Override
    public Page<Task> findTasksByUserIdAndProjectId(Long userId, Long projectId, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return taskJpaRepository.findTasksByProjectUserIdAndProjectId(userId, projectId, pageRequest);
    }

    @Override
    public Task save(Task task) {
        return taskJpaRepository.save(task);
    }

    @Override
    public Task findByIdAndUser(Long id, Long userId) {
        return taskJpaRepository.findTaskByIdAndUserId(id, userId)
            .orElseThrow(() -> new TaskNotFoundException(id, userId));
    }

    @Override
    public void deleteById(Long id) {
        taskJpaRepository.deleteById(id);
    }

    @Override
    public List<Task> findByFilter(Long userId, TaskFilter filter) {
        try (Session session = entityManager.unwrap(Session.class)) {
            PageRequest pageRequest = PageRequest.of(filter.getPage(), pageSize);

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
            Root<Task> task = criteriaQuery.from(Task.class);

            TaskJoinPredicates taskJoinPredicates = getJoinPredicates(task);

            List<Predicate> predicateList = new ArrayList<>(generateTaskPredicates(
                criteriaBuilder, filter, task, taskJoinPredicates));
            return createQueryAndGetResults(session, criteriaQuery, predicateList, pageRequest);
        }
    }

    private List<Predicate> generateTaskPredicates(CriteriaBuilder criteriaBuilder,
        TaskFilter filter,
        Root<Task> taskRoot,
        TaskJoinPredicates taskJoinPredicates) {
        List<Predicate> predicates = new ArrayList<>();
        addSimplePredicates(filter, taskRoot, predicates);
        addDateRangePredicates(criteriaBuilder, filter, taskRoot, predicates);
        addProjectPredicates(criteriaBuilder, filter, predicates, taskJoinPredicates);
        return predicates;
    }

    private void addProjectPredicates(CriteriaBuilder criteriaBuilder,
        TaskFilter filter,
        List<Predicate> predicates,
        TaskJoinPredicates taskJoinPredicates) {
        if (!CollectionUtils.isEmpty(filter.getProjectIds())) {
            filter.getProjectIds().forEach(projectId -> predicates.add(criteriaBuilder.or(
                criteriaBuilder.equal(taskJoinPredicates.getProjectJoin().get(Project_.id), projectId))));
        }
    }

    private void addSimplePredicates(TaskFilter filter, Root<Task> taskRoot, List<Predicate> predicates) {
        if (!CollectionUtils.isEmpty(filter.getStatus())) {
            Expression<TaskStatus> statusExpression = taskRoot.get(Task_.status);
            Predicate statusPredicate = statusExpression.in(filter.getStatus());
            predicates.add(statusPredicate);
        }
    }

    private void addDateRangePredicates(CriteriaBuilder criteriaBuilder,
        TaskFilter filter,
        Root<Task> taskRoot,
        List<Predicate> predicates) {
        DateRangeFilter startDateRangeFilter = filter.getDeadline();
        if (startDateRangeFilter != null && startDateRangeFilter.getDateFrom() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                taskRoot.get(Task_.deadline),
                startDateRangeFilter.getDateFrom()));
        }
        if (startDateRangeFilter != null && startDateRangeFilter.getDateTo() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(
                taskRoot.get(Task_.deadline),
                startDateRangeFilter.getDateTo()));
        }
    }

    private List<Task> createQueryAndGetResults(Session session,
        CriteriaQuery<Task> criteriaQuery,
        List<Predicate> predicateList,
        PageRequest page) {
        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
        TypedQuery<Task> query = session.createQuery(criteriaQuery);
        query.setFirstResult(Math.toIntExact(page.getOffset()));
        query.setMaxResults(page.getPageSize());
        return query.getResultList();
    }

    private TaskJoinPredicates getJoinPredicates(Root<Task> taskRoot) {
        TaskJoinPredicates taskJoinPredicates = new TaskJoinPredicates();
        taskJoinPredicates.setProjectJoin(taskRoot.join(Task_.project, JoinType.LEFT));
        return taskJoinPredicates;
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    private static class TaskJoinPredicates {
        private Join<Task, Project> projectJoin;
    }
}
