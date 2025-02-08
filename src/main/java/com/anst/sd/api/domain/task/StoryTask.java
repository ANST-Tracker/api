package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "story_task")
@Getter
@Setter
public class StoryTask extends AbstractTask {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FullCycleStatus status;
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

    public StoryTask() {
        this.status = FullCycleStatus.OPEN;
    }

    @Override
    public void updateFrom(AbstractTask source, UUID userId) {
        super.updateFrom(source, userId);
        if (source instanceof StoryTask storySource) {
            this.status = storySource.getStatus();
            this.tester = storySource.getTester();
            this.sprint = storySource.getSprint();
            this.epicTask = storySource.getEpicTask();
        }
    }
}
