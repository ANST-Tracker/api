package com.anst.sd.api.domain;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class BusinessEntity extends DomainObject {
    @Column(name = "created_at", nullable = false)
    private Instant created;
    @Column(name = "updated_at")
    private Instant updated;
}
