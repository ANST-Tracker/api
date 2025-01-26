package com.anst.sd.api.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.TASK_TITLE;
import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.USER_LOGIN;

@Getter
@AllArgsConstructor
public enum NotificationTemplate {
    TASK_NEW_ASSIGNEE(
        "Назначена новая задача",
        """
        ${userLogin} назначил на тебя задачу "${taskTitle}"
        ${link}
        """,
        Set.of(USER_LOGIN, TASK_TITLE)
    );

    private final String title;
    private final String body;
    private final Set<AdditionalTemplateParam> additionalParams;

    @RequiredArgsConstructor
    @Getter
    public enum AdditionalTemplateParam {
        USER_LOGIN("userLogin"),
        TASK_TITLE("taskTitle"),
        LINK("link");

        private final String key;
    }
}
