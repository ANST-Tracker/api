package com.anst.sd.api.domain.task;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "epic_task")
@Getter
@Setter
public class EpicTask extends AbstractTask {
    @OneToMany(mappedBy = "epicTask")
    private List<StoryTask> stories;
}
