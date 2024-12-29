package com.anst.sd.api.domain.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subtask")
@Getter
@Setter
public class Subtask extends AbstractTask {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShortCycleStatus status;
    @ManyToOne
    @JoinColumn(name = "story_task_id")
    private StoryTask storyTask;
}
