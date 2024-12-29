package com.anst.sd.api.domain;

import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users_projects")
@Getter
@Setter
public class UsersProjects extends BusinessEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(name = "permission_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionCode permissionCode;
}
