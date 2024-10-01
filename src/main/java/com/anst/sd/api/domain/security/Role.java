package com.anst.sd.api.domain.security;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.security.domain.ERole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role extends DomainObject {
    @Enumerated(EnumType.STRING)
    @Column
    private ERole name;
}
