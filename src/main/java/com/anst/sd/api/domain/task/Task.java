package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "task")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    @JoinColumn(name = "user_id")
    private User user;
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

    @JsonIgnore
    public User getUser() {
        return user;
    }
}
