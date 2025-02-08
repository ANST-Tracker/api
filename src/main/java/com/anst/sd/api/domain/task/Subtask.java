package com.anst.sd.api.domain.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

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

    public Subtask() {
        this.status = ShortCycleStatus.OPEN;
    }

    @Override
    public void updateFrom(AbstractTask source, UUID userId) {
        super.updateFrom(source, userId);
        if (source instanceof Subtask subtaskSource) {
            this.status = subtaskSource.getStatus();
            this.storyTask = subtaskSource.getStoryTask();
        }
    }
}
