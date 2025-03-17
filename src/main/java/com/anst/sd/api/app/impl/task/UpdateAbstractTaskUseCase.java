package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.UpdateAbstractTaskInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.DefectTask;
import com.anst.sd.api.domain.task.StoryTask;
import com.anst.sd.api.domain.task.Subtask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateAbstractTaskUseCase implements UpdateAbstractTaskInBound {
    private final AbstractTaskRepository abstractTaskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public AbstractTask update(UUID userId, String simpleId, AbstractTask updated) {
        log.info("Updating task with id {} and userId {}", simpleId, userId);
        AbstractTask original = abstractTaskRepository.getBySimpleId(simpleId);
        mergeTask(original, updated);
        mergeSpecificFields(original, updated);
        return abstractTaskRepository.save(original);
    }

    private void mergeTask(AbstractTask original, AbstractTask updated) {
        original.setName(updated.getName());
        original.setDescription(updated.getDescription());
        original.setType(updated.getType());
        original.setPriority(updated.getPriority());
        original.setStoryPoints(updated.getStoryPoints());
        if (updated.getAssignee() != null && updated.getAssignee().getId() != null) {
            original.setAssignee(userRepository.getById(updated.getAssignee().getId()));
        }
        if (updated.getReviewer() != null && updated.getReviewer().getId() != null) {
            original.setReviewer(userRepository.getById(updated.getReviewer().getId()));
        }
        if (updated.getProject() != null && updated.getProject().getId() != null) {
            original.setProject(projectRepository.getById(updated.getProject().getId()));
        }
        original.setDueDate(updated.getDueDate());
        original.setTimeEstimation(updated.getTimeEstimation());
        original.setTags(tagRepository.findAllByIdInAndProjectId(updated.getTags().stream().map(Tag::getId).toList(),
                original.getProject().getId()));
        // TODO: After sprint realization, needs to update sprint parameters in updated request
    }

    private void mergeSpecificFields(AbstractTask original, AbstractTask updated) {
        if (original instanceof Subtask originalSubtask && updated instanceof Subtask subtask) {
            originalSubtask.setStoryTask(subtask.getStoryTask());
        }
        if (original instanceof StoryTask originalStory && updated instanceof StoryTask story) {
            originalStory.setEpicTask(story.getEpicTask());
            originalStory.setTester(story.getTester());
            originalStory.setSprint(story.getSprint());
        }
        if (original instanceof DefectTask originalDefect && updated instanceof DefectTask defect) {
            originalDefect.setStoryTask(defect.getStoryTask());
            originalDefect.setTester(defect.getTester());
            originalDefect.setSprint(defect.getSprint());
        }
    }
}
