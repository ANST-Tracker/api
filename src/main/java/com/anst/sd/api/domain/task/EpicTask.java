package com.anst.sd.api.domain.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
}
