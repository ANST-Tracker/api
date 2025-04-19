package com.anst.sd.api.domain.sprint;

import com.anst.sd.api.domain.BusinessEntity;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.DefectTask;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sprint")
@Getter
@Setter
@Accessors(chain = true)
public class Sprint extends BusinessEntity {
    @Column(nullable = false)
    private String name;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Column
    private String description;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @OneToMany(mappedBy = "sprint")
    private List<DefectTask> defects;
    @OneToMany(mappedBy = "sprint")
    private List<DefectTask> stories;
}
