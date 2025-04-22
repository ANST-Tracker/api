package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.project.ProjectValidationException;
import com.anst.sd.api.app.api.sprint.SprintRepository;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.AbstractTaskValidationException;
import com.anst.sd.api.app.api.task.CreateAbstractTaskInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.task.*;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateAbstractTaskUseCase implements CreateAbstractTaskInBound {
    private final AbstractTaskRepository abstractTaskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final SprintRepository sprintRepository;

    @Override
    @Transactional
    public AbstractTask create(UUID userId, AbstractTask task) {
        log.info("Creating task with userId {}", userId);
        Project project = projectRepository.getById(task.getProject().getId());
        User creator = userRepository.getById(userId);
        User reviewer = userRepository.getById(task.getReviewer().getId());
        User assignee = userRepository.getById(task.getAssignee().getId());
        List<Tag> tags = tagRepository.findAllByIdInAndProjectId(task.getTags().stream().map(Tag::getId).toList(),
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
            validateSubtask(subtask);
            StoryTask parentStory = (StoryTask) abstractTaskRepository.getByIdAndProjectId(subtask.getStoryTask().getId(),
                    project.getId());
            subtask.setStoryTask(parentStory);
        }

        if (task instanceof StoryTask storyTask) {
            validateUserHasAccessToProject(userId, project.getId());
            validateStoryTask(storyTask);
            Sprint sprint = sprintRepository.getByIdAndProjectId(storyTask.getSprint().getId(), project.getId());
            EpicTask parentEpic = (EpicTask) abstractTaskRepository.getByIdAndProjectId(storyTask.getEpicTask().getId(),
                    project.getId());
            storyTask.setEpicTask(parentEpic);
            storyTask.setSprint(sprint);
        }

        if (task instanceof DefectTask defectTask) {
            validateDefectTask(defectTask);
            Sprint sprint = sprintRepository.getById(defectTask.getSprint().getId());
            StoryTask parentStory = (StoryTask) abstractTaskRepository.getByIdAndProjectId(defectTask.getStoryTask().getId(),
                    project.getId());
            defectTask.setSprint(sprint);
            defectTask.setStoryTask(parentStory);
        }

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
        if (!projectRepository.existsByIdAndUserId(userId, projectId)) {
            throw new ProjectValidationException(userId, projectId);
        }
    }
}
