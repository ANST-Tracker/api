package com.anst.sd.api.domain.task;

import lombok.Getter;

@Getter
public enum TaskPriority {
    BLOCKER("Blocker"),
    CRITICAL("Critical"),
    MAJOR("Major"),
    MINOR("Minor"),
    TRIVIAL("Trivial");

    private final String value;

    TaskPriority(String value) {
        this.value = value;
    }
}
