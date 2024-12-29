package com.anst.sd.api.domain.user;

import lombok.Getter;

@Getter
public enum Position {
    QA("Тестировщик"),
    PM("Менеджер проектов"),
    HEAD("Директор"),
    DEV("Разработчик"),
    ANALYTICS("Аналитик"),
    DEVOPS("Инженер");

    private final String value;

    Position(String value) {
        this.value = value;
    }
}
