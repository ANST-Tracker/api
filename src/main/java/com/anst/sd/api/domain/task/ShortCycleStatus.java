package com.anst.sd.api.domain.task;

import lombok.Getter;

@Getter
public enum ShortCycleStatus {
    OPEN("Открыта"),
    IN_PROGRESS("В работе"),
    REVIEW("На ревью"),
    CLOSED("Закрыта");

    private final String value;

    ShortCycleStatus(String value) {
        this.value = value;
    }
}
