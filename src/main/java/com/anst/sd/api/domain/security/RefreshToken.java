package com.anst.sd.api.domain.security;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken extends DomainObject {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;
    @Column(nullable = false)
    private Instant created;

    @PrePersist
    public void prePersist() {
        created = Instant.now();
    }

}