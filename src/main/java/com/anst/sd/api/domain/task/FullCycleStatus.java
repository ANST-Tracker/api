package com.anst.sd.api.domain.task;

import lombok.Getter;

@Getter
public enum FullCycleStatus {
    OPEN("Открыта"),
    IN_PROGRESS("В работе"),
    REVIEW("На ревью"),
    RESOLVED("Разрешена"),
    QA_READY("Готова к тестированию"),
    IN_QA("В тесте"),
    CLOSED("Закрыта");

    private final String value;

    FullCycleStatus(String value) {
        this.value = value;
    }
}
