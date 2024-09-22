package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.notification.PendingNotification;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.tag.Tag;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "task")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
public class Task extends DomainObject {
    @Column
    private String data;
    @Column
    private LocalDateTime deadline;
    @Column
    private String description;
    @Column
    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToMany
    @JoinTable(
            name = "task_tags", joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "task", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PendingNotification> pendingNotifications = new ArrayList<>();
    @Column(nullable = false)
    private Instant created;
    @Column
    private Instant updated;

    @PrePersist
    public void prePersist() {
        created = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updated = Instant.now();
    }

    @Transient
    Long updatedProjectId;

    public void setPendingNotifications(List<PendingNotification> pendingNotifications) {
        this.pendingNotifications.clear();
        if (pendingNotifications != null) {
            this.pendingNotifications.addAll(pendingNotifications);
            for (PendingNotification p : this.pendingNotifications) {
                p.setTask(this);
            }
        }
    }
}
