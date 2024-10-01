package com.anst.sd.api.domain.project;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "project")
public class Project extends DomainObject {
  @Column(nullable = false)
  private String name;
  @Column(name = "type", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private ProjectType projectType;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
  private List<Task> tasks;
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
}
