package com.anst.sd.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class DomainObject {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;
}
