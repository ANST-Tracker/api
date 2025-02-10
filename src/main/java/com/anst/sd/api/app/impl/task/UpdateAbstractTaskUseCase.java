package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.UpdateAbstractTaskInBound;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.task.DefectTask;
import com.anst.sd.api.domain.task.StoryTask;
import com.anst.sd.api.domain.task.Subtask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateAbstractTaskUseCase implements UpdateAbstractTaskInBound {
    private final AbstractTaskRepository abstractTaskRepository;


    @Override
    public AbstractTask update(UUID userId, UUID taskId, AbstractTask updated) {
        AbstractTask original = abstractTaskRepository.findById(taskId);
        mergeTask(original, updated);
        mergeSpecificFields(original, updated);
        return abstractTaskRepository.save(original);
    }

    private void mergeTask(AbstractTask original, AbstractTask updated) {
        if (!updated.getName().isEmpty()) {
            original.setName(updated.getName());
        }
        original.setDescription(updated.getDescription());
        if (updated.getType() != null) {
            original.setType(updated.getType());
        }
        if (updated.getPriority() != null) {
            original.setPriority(updated.getPriority());
        }
        if (updated.getStoryPoints() != null) {
            original.setStoryPoints(updated.getStoryPoints());
        }
        if (updated.getAssignee() != null) {
            original.setAssignee(updated.getAssignee());
        }
        if (updated.getReviewer() != null) {
            original.setReviewer(updated.getReviewer());
        }
        if (updated.getCreator() != null) {
            original.setCreator(updated.getCreator());
        }
        original.setProject(updated.getProject());
        original.setDueDate(updated.getDueDate());
        original.setTimeEstimation(updated.getTimeEstimation());
        original.setTags(updated.getTags());
    }

    private void mergeSpecificFields(AbstractTask original, AbstractTask updated) {
        if (updated instanceof Subtask subtask) {
            ((Subtask) original).setStoryTask(subtask.getStoryTask());
        }
        if (updated instanceof StoryTask story) {
            ((StoryTask) original).setEpicTask(story.getEpicTask());
            ((StoryTask) original).setTester(story.getTester());
            ((StoryTask) original).setSprint(story.getSprint());
        }
        if (updated instanceof DefectTask defect) {
            ((DefectTask) original).setStoryTask(defect.getStoryTask());
            ((DefectTask) original).setTester(defect.getTester());
            ((DefectTask) original).setSprint(defect.getSprint());
        }
    }
}
