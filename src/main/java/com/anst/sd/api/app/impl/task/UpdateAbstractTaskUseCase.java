package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.UpdateAbstractTaskInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.app.impl.notification.NotificationCreatedEvent;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.task.*;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.*;
import static com.anst.sd.api.domain.notification.NotificationTemplate.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateAbstractTaskUseCase implements UpdateAbstractTaskInBound {
    private final AbstractTaskRepository abstractTaskRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public AbstractTask update(UUID userId, String simpleId, AbstractTask updated) {
        log.info("Updating task with id {} and userId {}", simpleId, userId);
        AbstractTask original = abstractTaskRepository.getBySimpleId(simpleId);
        mergeTask(original, updated, userRepository.getById(userId));
        mergeSpecificFields(original, updated);
        return abstractTaskRepository.save(original);
    }

    private void mergeTask(AbstractTask original, AbstractTask updated, User changer) {
        Collection<User> originalUsers = Stream.of(
                        original.getAssignee(),
                        original.getReviewer(),
                        original.getCreator())
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(User::getId, user -> user, (a, b) -> a))
                .values();

        original.setName(updated.getName());
        original.setDescription(updated.getDescription());
        original.setType(updated.getType());
        original.setPriority(updated.getPriority());
        original.setStoryPoints(updated.getStoryPoints());
        original.setDueDate(updated.getDueDate());
        original.setTimeEstimation(updated.getTimeEstimation());

        User oldAssignee = original.getAssignee();
        if (updated.getAssignee() != null && updated.getAssignee().getId() != null) {
            original.setAssignee(userRepository.getById(updated.getAssignee().getId()));
        } else {
            original.setAssignee(null);
        }
        createAssigneeUpdatedNotification(original, oldAssignee, originalUsers, changer);

        User oldReviewer = original.getReviewer();
        if (updated.getReviewer() != null && updated.getReviewer().getId() != null) {
            original.setReviewer(userRepository.getById(updated.getAssignee().getId()));
        } else {
            original.setReviewer(null);
        }
        createReviewerUpdatedNotification(original, oldReviewer, originalUsers, changer);

        if (!CollectionUtils.isEmpty(updated.getTags())) {
            original.setTags(tagRepository.findAllByIdInAndProjectId(updated.getTags().stream().map(Tag::getId).toList(),
                    original.getProject().getId()));
        }
        // TODO: After sprint realization, needs to update sprint parameters in updated request
    }

    private void mergeSpecificFields(AbstractTask original, AbstractTask updated) {
        if (original instanceof Subtask originalSubtask && updated instanceof Subtask subtask) {
            originalSubtask.setStoryTask((StoryTask) abstractTaskRepository.getByIdAndProjectId(
                    subtask.getStoryTask().getId(), originalSubtask.getProject().getId()));
        }
        if (original instanceof StoryTask originalStory && updated instanceof StoryTask story) {
            originalStory.setEpicTask((EpicTask) abstractTaskRepository.getByIdAndProjectId(
                    story.getEpicTask().getId(), originalStory.getProject().getId()));
            if (story.getTester() != null && story.getTester().getId() != null) {
                originalStory.setTester(userRepository.getById(story.getTester().getId()));
            } else {
                originalStory.setTester(null);
            }
            if (story.getSprint() != null && story.getSprint().getId() != null) {
                //TODO
                originalStory.setSprint(story.getSprint());
            } else {
                originalStory.setSprint(null);
            }
        }
        if (original instanceof DefectTask originalDefect && updated instanceof DefectTask defect) {
            if (defect.getStoryTask() != null && defect.getStoryTask().getId() != null) {
                defect.setStoryTask((StoryTask) abstractTaskRepository.getByIdAndProjectId(
                        defect.getStoryTask().getId(), originalDefect.getProject().getId()));
            } else {
                defect.setStoryTask(null);
            }
            if (defect.getTester() != null && defect.getTester().getId() != null) {
                originalDefect.setTester(userRepository.getById(defect.getTester().getId()));
            } else {
                originalDefect.setTester(null);
            }
            if (defect.getSprint() != null && defect.getSprint().getId() != null) {
                //TODO
                originalDefect.setSprint(defect.getSprint());
            } else {
                originalDefect.setSprint(null);
            }
        }
    }

    private void createAssigneeUpdatedNotification(AbstractTask task, User oldUser, Collection<User> recipients, User author) {
        if (task.getAssignee() != null
                && task.getAssignee().getId() != null
                && oldUser != null
                && Objects.equals(oldUser.getId(), task.getAssignee().getId())) {
            return;
        }
        if (task.getAssignee() == null && oldUser == null) {
            return;
        }
        List<NotificationCreatedEvent> baseEvents = recipients.stream().map(user -> {
                    NotificationCreatedEvent event = new NotificationCreatedEvent();
                    event.setRecipient(user);
                    event.setParams(new HashMap<>(Map.of(
                            TASK_SIMPLE_ID.getKey(), task.getSimpleId(),
                            TASK_TITLE.getKey(), task.getName(),
                            USER_NAME.getKey(), "%s %s".formatted(author.getFirstName(), author.getLastName())
                    )));
                    return event;
                })
                .toList();
        if (task.getAssignee() == null) {
            baseEvents.forEach(event -> {
                event.setTemplate(TASK_ASSIGNEE_REMOVED);
                event.getParams().put(
                        OLD_USER_NAME.getKey(), "%s %s".formatted(oldUser.getFirstName(), oldUser.getLastName())
                );
            });
        } else {
            baseEvents.forEach(event -> {
                event.setTemplate(NEW_TASK_ASSIGNEE);
                event.getParams().put(
                        NEW_USER_NAME.getKey(), "%s %s".formatted(task.getAssignee().getFirstName(), task.getAssignee().getLastName())
                );
            });
        }
        baseEvents.forEach(applicationEventPublisher::publishEvent);
    }

    private void createReviewerUpdatedNotification(AbstractTask task, User oldUser, Collection<User> recipients, User author) {
        if (task.getReviewer() != null
                && task.getReviewer().getId() != null
                && oldUser != null
                && Objects.equals(oldUser.getId(), task.getReviewer().getId())) {
            return;
        }
        if (task.getReviewer() == null && oldUser == null) {
            return;
        }
        List<NotificationCreatedEvent> baseEvents = recipients.stream().map(user -> {
                    NotificationCreatedEvent event = new NotificationCreatedEvent();
                    event.setRecipient(user);
                    event.setParams(new HashMap<>(Map.of(
                            TASK_SIMPLE_ID.getKey(), task.getSimpleId(),
                            TASK_TITLE.getKey(), task.getName(),
                            USER_NAME.getKey(), "%s %s".formatted(author.getFirstName(), author.getLastName())
                    )));
                    return event;
                })
                .toList();
        if (task.getReviewer() == null) {
            baseEvents.forEach(event -> {
                event.setTemplate(TASK_REVIEWER_REMOVED);
                event.getParams().put(
                        OLD_USER_NAME.getKey(), "%s %s".formatted(oldUser.getFirstName(), oldUser.getLastName())
                );
            });
        } else {
            baseEvents.forEach(event -> {
                event.setTemplate(NEW_TASK_REVIEWER);
                event.getParams().put(
                        NEW_USER_NAME.getKey(), "%s %s".formatted(task.getReviewer().getFirstName(), task.getReviewer().getLastName())
                );
            });
        }
        baseEvents.forEach(applicationEventPublisher::publishEvent);
    }
}
