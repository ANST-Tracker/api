package com.anst.sd.api.domain.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@NamedEntityGraph(
        name = "epic-task-full",
        attributeNodes = {
                @NamedAttributeNode(value = "reviewer"),
                @NamedAttributeNode(value = "assignee"),
                @NamedAttributeNode(value = "creator"),
                @NamedAttributeNode(value = "project"),
                @NamedAttributeNode(value = "tags"),
                @NamedAttributeNode(value = "timeEstimation")
        })
@Entity
@Table(name = "epic_task")
@Getter
@Setter
public class EpicTask extends AbstractTask {
    @OneToMany(mappedBy = "epicTask")
    private List<StoryTask> stories;
}
