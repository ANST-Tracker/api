package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "story_task")
@Getter
@Setter
@Accessors(chain = true)
public class StoryTask extends AbstractTask {
    @ManyToOne
    @JoinColumn(name = "tester_id")
    private User tester;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;
    @ManyToOne
    @JoinColumn(name = "epic_task_id")
    private EpicTask epicTask;
    @OneToMany(mappedBy = "storyTask")
    private List<DefectTask> defects;
}
