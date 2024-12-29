package com.anst.sd.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class TimeEstimation {
    @Column
    @Enumerated(EnumType.STRING)
    private TimeUnit timeUnit;
    @Column
    private Integer amount;
}
