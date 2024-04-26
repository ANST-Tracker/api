package com.anst.sd.api.domain.notification;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.task.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "pending_notification")
public class PendingNotification extends DomainObject {
    @Column(name = "remind", nullable = false)
    private Instant remindIn;
    @Column(nullable = false)
    private int amount;
    @Column(name = "time_type", nullable = false)
    private TimeUnit timeType;
    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonBackReference
    private Task task;
}