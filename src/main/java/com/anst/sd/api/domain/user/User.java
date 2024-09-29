package com.anst.sd.api.domain.user;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.security.Device;
import com.anst.sd.api.domain.security.Role;
import com.anst.sd.api.domain.tag.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "user")
@Table(name = "users")
public class User extends DomainObject {
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String username;
    @Column(nullable = false)
    @NotBlank
    private String password;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String telegramId;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Project> projects;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Device> devices;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Tag> tags;
    @Column(nullable = false)
    private Instant created;
    @Column
    private Instant updated;

    public User(String username, String telegramId, String password) {
        this.username = username;
        this.telegramId = telegramId;
        this.password = password;
    }

    @PrePersist
    public void prePersist() {
        created = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updated = Instant.now();
    }
}
