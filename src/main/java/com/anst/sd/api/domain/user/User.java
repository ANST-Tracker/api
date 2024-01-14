package com.anst.sd.api.domain.user;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.security.Device;
import com.anst.sd.api.domain.security.RefreshToken;
import com.anst.sd.api.domain.security.Role;
import com.anst.sd.api.domain.task.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
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
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<RefreshToken> tokens = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Device> devices = new ArrayList<>();
    @Column(nullable = false)
    private Instant created;
    @Column
    private Instant updated;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
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
