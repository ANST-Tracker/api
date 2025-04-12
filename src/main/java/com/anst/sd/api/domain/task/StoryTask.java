package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@NamedEntityGraph(
        name = "story-task-full",
        attributeNodes = {
                @NamedAttributeNode(value = "reviewer"),
                @NamedAttributeNode(value = "assignee"),
                @NamedAttributeNode(value = "creator"),
                @NamedAttributeNode(value = "project"),
                @NamedAttributeNode(value = "tags"),
                @NamedAttributeNode(value = "tester"),
                @NamedAttributeNode(value = "sprint"),
                @NamedAttributeNode(value = "epicTask"),
        })
@Entity
@Table(name = "story_task")
@Getter
@Setter
@Accessors(chain = true)
public class StoryTask extends AbstractTask {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tester_id")
    private User tester;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "epic_task_id")
    private EpicTask epicTask;
    @OneToMany(mappedBy = "storyTask")
    private List<DefectTask> defects;
}
