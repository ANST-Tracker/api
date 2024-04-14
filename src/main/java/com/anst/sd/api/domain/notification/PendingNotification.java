package com.anst.sd.api.domain.notification;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.task.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "pending_notification")
@Table(name = "pending_notifications")
public class PendingNotification extends DomainObject {
    @Column(nullable = false)
    private LocalDateTime remindIn;
    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonBackReference
    private Task task;
}