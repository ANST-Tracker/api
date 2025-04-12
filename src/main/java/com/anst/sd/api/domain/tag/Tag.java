package com.anst.sd.api.domain.tag;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.AbstractTask;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "tag")
@Getter
@Setter
@Accessors(chain = true)
public class Tag extends DomainObject {
    @Column(nullable = false)
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToMany(mappedBy = "tags")
    private List<AbstractTask> tasks;

    @PreRemove
    private void removeTagFromTasks() {
        if (tasks != null) {
            tasks.forEach(task -> task.getTags().remove(this));
        }
    }
}
