package com.anst.sd.api.domain.notification;

import com.anst.sd.api.domain.DomainObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "notification")
public class Notification extends DomainObject {
  @Column(name = "execution_date", nullable = false)
  private Instant executionDate;
  @Column(name = "task_name", nullable = false)
  private String taskName;
  @Column(name = "project_name")
  private String projectName;
  @Column(name = "task_id", nullable = false)
  private Long taskId;
}