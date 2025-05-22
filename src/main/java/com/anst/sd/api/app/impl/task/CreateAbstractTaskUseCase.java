package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.project.ProjectValidationException;
import com.anst.sd.api.app.api.sprint.SprintRepository;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.AbstractTaskValidationException;
import com.anst.sd.api.app.api.task.CreateAbstractTaskInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.app.impl.notification.NotificationCreatedEvent;
import com.anst.sd.api.domain.notification.NotificationTemplate;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.task.*;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateAbstractTaskUseCase implements CreateAbstractTaskInBound {
    private final AbstractTaskRepository abstractTaskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SprintRepository sprintRepository;

    @Override
    @Transactional
    public AbstractTask create(UUID userId, AbstractTask task) {
        log.info("Creating task with userId {}", userId);
        if (task == null) {
            throw new AbstractTaskValidationException("Task must not be null");
        }
        if (task.getProject() == null || task.getProject().getId() == null) {
            throw new AbstractTaskValidationException("Project is required");
        }
        Project project = projectRepository.getById(task.getProject().getId());
        project.setNextTaskId(project.getNextTaskId() + 1);
        validateUserHasAccessToProject(userId, project.getId());
        User creator = userRepository.getById(userId);
        User reviewer = task.getReviewer() != null && task.getReviewer().getId() != null
                ? userRepository.getById(task.getReviewer().getId()) : null;
        User assignee = task.getAssignee() != null && task.getAssignee().getId() != null
                ? userRepository.getById(task.getAssignee().getId()) : null;
        List<Tag> tags = CollectionUtils.isEmpty(task.getTags())
                ? Collections.emptyList() : tagRepository.findAllByIdInAndProjectId(
                task.getTags().stream()
                        .filter(t -> t != null && t.getId() != null)
                        .map(Tag::getId)
                        .toList(),
                project.getId());
        BigDecimal orderNumber = abstractTaskRepository.findNextOrderNumber(task.getId());
        task.setAssignee(assignee);
        task.setProject(project);
        task.setReviewer(reviewer);
        task.setTags(tags);
        task.setSimpleId(SimpleIdGenerationDelegate.idGenerator(task));
        task.setOrderNumber(orderNumber.add(BigDecimal.ONE));
        task.setCreator(creator);
        if (task instanceof Subtask subtask) {
            if (subtask.getStoryTask() != null && subtask.getStoryTask().getId() != null) {
                StoryTask parentStory = (StoryTask) abstractTaskRepository.getByIdAndProjectId(subtask.getStoryTask().getId(),
                        project.getId());
                subtask.setStoryTask(parentStory);
            } else {
                subtask.setStoryTask(null);
            }
            validateSubtask(subtask);
        }

        if (task instanceof StoryTask storyTask) {
            validateStoryTask(storyTask);
            EpicTask parentEpic = (EpicTask) abstractTaskRepository.getByIdAndProjectId(storyTask.getEpicTask().getId(),
                    project.getId());
            storyTask.setEpicTask(parentEpic);
            if (storyTask.getSprint() != null && storyTask.getSprint().getId() != null) {
                Sprint sprint = sprintRepository.getByIdAndProjectId(storyTask.getSprint().getId(), project.getId());
                storyTask.setSprint(sprint);
            }
        }

        if (task instanceof DefectTask defectTask) {
            Sprint sprint = sprintRepository.getById(defectTask.getSprint().getId());
            StoryTask parentStory = (StoryTask) abstractTaskRepository.getByIdAndProjectId(defectTask.getStoryTask().getId(),
                    project.getId());
            defectTask.setSprint(sprint);
            defectTask.setStoryTask(parentStory);
            validateDefectTask(defectTask);
        }

        sendNotification(task);
        return abstractTaskRepository.save(task);
    }

    private void validateSubtask(AbstractTask task) {
        if (task instanceof Subtask subtask && subtask.getStoryTask() == null) {
            throw new AbstractTaskValidationException();
        }
    }

    private void validateStoryTask(AbstractTask task) {
        if (task instanceof StoryTask storyTask && storyTask.getEpicTask() == null) {
            throw new AbstractTaskValidationException();
        }
    }

    private void validateDefectTask(AbstractTask task) {
        if (task instanceof DefectTask defectTask && defectTask.getStoryTask() == null) {
            throw new AbstractTaskValidationException();
        }
    }

    private void validateUserHasAccessToProject(UUID userId, UUID projectId) {
        if (!projectRepository.existsByIdAndUserId(projectId, userId)) {
            throw new ProjectValidationException(userId, projectId);
        }
    }

    private void sendNotification(AbstractTask task) {
        User owner = task.getProject().getHead();
        NotificationCreatedEvent event = new NotificationCreatedEvent();
        event.setRecipient(owner);
        event.setParams(Map.of(
                TASK_TITLE.getKey(), task.getName(),
                TASK_TYPE.getKey(), task.getType().getValue(),
                USER_NAME.getKey(), "%s %s".formatted(task.getCreator().getFirstName(), task.getCreator().getLastName()),
                PROJECT_NAME.getKey(), task.getProject().getName()
        ));
        event.setTemplate(NotificationTemplate.NEW_TASK_IN_PROJECT);
        applicationEventPublisher.publishEvent(event);
    }
}
