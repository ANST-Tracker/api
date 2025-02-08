package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.TimeEstimation;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "abstract_task")
@Getter
@Setter
public abstract class AbstractTask extends DomainObject {

    @Column(name = "simple_id", nullable = false)
    private Integer simpleId;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(name = "story_points")
    private Integer storyPoints;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "order_number", nullable = false)
    private BigDecimal orderNumber;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "time_estimation")
    private TimeEstimation timeEstimation;

    @ManyToMany
    @JoinTable(
            name = "tasks_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    public void updateFrom(AbstractTask source, UUID userId) {
        this.simpleId = source.getSimpleId();
        this.name = source.getName();
        this.description = source.getDescription();
        this.type = source.getType();
        this.priority = source.getPriority();
        this.storyPoints = source.getStoryPoints();
        this.assignee = source.getAssignee();
        this.reviewer = source.getReviewer();
        this.creator = source.getCreator();
        this.project = source.getProject();
        this.dueDate = source.getDueDate();
        this.orderNumber = source.getOrderNumber();
        this.timeEstimation = source.getTimeEstimation();
        this.tags = source.getTags();
    }
}