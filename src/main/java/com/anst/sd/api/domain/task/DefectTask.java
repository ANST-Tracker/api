package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "defect_task")
@Getter
@Setter
public class DefectTask extends AbstractTask {
    @ManyToOne
    @JoinColumn(name = "tester_id")
    private User tester;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;
    @ManyToOne
    @JoinColumn(name = "story_task_id")
    private StoryTask storyTask;
}
