package com.anst.sd.api.domain.user;

import com.anst.sd.api.domain.BusinessEntity;
import com.anst.sd.api.domain.UsersProjects;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Table(name = "users")
@Entity
public class User extends BusinessEntity {
    @Column(nullable = false)
    private String username;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(name = "telegram_id", nullable = false)
    private String telegramId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;
    @Column(name = "department_name", nullable = false)
    private String departmentName;
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
    @Column(name = "time_zone", nullable = false)
    private Integer timeZone;
    @Column(name = "profile_picture_id")
    private String profilePictureId;
    @OneToMany(mappedBy = "user")
    private List<UsersProjects> projects;
}
