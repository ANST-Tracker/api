package com.anst.sd.api.domain.task;

import lombok.Getter;

@Getter
public enum TaskType {
    DEFECT("Дефект"),
    STORY("Стори"),
    EPIC("Эпик стори"),
    SUBTASK("Подзадача");

    private final String value;

    TaskType(String value) {
        this.value = value;
    }
}
