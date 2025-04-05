package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@NamedEntityGraph(
        name = "defect-task-full",
        attributeNodes = {
                @NamedAttributeNode(value = "reviewer"),
                @NamedAttributeNode(value = "assignee"),
                @NamedAttributeNode(value = "creator"),
                @NamedAttributeNode(value = "project"),
                @NamedAttributeNode(value = "tags"),
                @NamedAttributeNode(value = "tester"),
                @NamedAttributeNode(value = "sprint"),
                @NamedAttributeNode(value = "storyTask"),
        })
@Entity
@Table(name = "defect_task")
@Getter
@Setter
public class DefectTask extends AbstractTask {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tester_id")
    private User tester;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_task_id")
    private StoryTask storyTask;
}
