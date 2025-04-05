package com.anst.sd.api.domain.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@NamedEntityGraph(
        name = "subtasktask-full",
        attributeNodes = {
                @NamedAttributeNode(value = "reviewer"),
                @NamedAttributeNode(value = "assignee"),
                @NamedAttributeNode(value = "creator"),
                @NamedAttributeNode(value = "project"),
                @NamedAttributeNode(value = "tags"),
                @NamedAttributeNode(value = "storyTask")
        })
@Entity
@Table(name = "subtask")
@Getter
@Setter
public class Subtask extends AbstractTask {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_task_id")
    private StoryTask storyTask;
}
