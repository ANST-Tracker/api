package com.anst.sd.api.domain.project;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.UsersProjects;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
public class Project extends DomainObject {
    @Column(nullable = false)
    private String name;
    @Column
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(name = "head_id")
    private User head;
    @Column(name = "next_task_id", nullable = false)
    private Integer nextTaskId;
    @Column(nullable = false)
    private String key;
    @OneToMany(mappedBy = "project")
    private List<Sprint> sprints;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersProjects> users;
    @OneToMany(mappedBy = "project")
    private List<Tag> tags;
}
