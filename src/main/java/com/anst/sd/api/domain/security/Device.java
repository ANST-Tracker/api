package com.anst.sd.api.domain.security;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Setter
@Getter
@Table(name = "device")
public class Device extends DomainObject {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column
    private String token;
    @Column(name = "user_agent")
    private String userAgent;
    @Column
    private String ip;

    @Column(name = "last_login")
    private Instant lastLogin;
    @Column(nullable = false)
    private Instant created;

    @PrePersist
    public void prePersist() {
        created = Instant.now();
        lastLogin = created;
    }

    @PreUpdate
    public void preUpdate() {
        lastLogin = Instant.now();
    }

    public static Device createDevice(User user) {
        Device device = new Device();
        device.user = user;
        return device;
    }
}