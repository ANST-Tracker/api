package com.anst.sd.api.domain.task;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subtask")
@Getter
@Setter
public class Subtask extends AbstractTask {
    @ManyToOne
    @JoinColumn(name = "story_task_id")
    private StoryTask storyTask;
}
