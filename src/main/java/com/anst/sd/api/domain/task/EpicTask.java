package com.anst.sd.api.domain.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "epic_task")
@Getter
@Setter
public class EpicTask extends AbstractTask {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShortCycleStatus status;
    @OneToMany(mappedBy = "epicTask")
    private List<StoryTask> stories;

    public EpicTask() {
        this.status = ShortCycleStatus.OPEN;
    }

    @Override
    public void updateFrom(AbstractTask source, UUID userId) {
        super.updateFrom(source, userId);
        if (source instanceof EpicTask epicSource) {
            this.status = epicSource.getStatus();
        }
    }
}
