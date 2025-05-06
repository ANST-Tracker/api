package com.anst.sd.api.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.*;

@Getter
@AllArgsConstructor
public enum NotificationTemplate {
    NEW_TASK_IN_PROJECT(
            "Новая задача в проекте",
            """
                    ${userName} создал задачу ${taskTitle} с типом ${taskType} в проекте ${projectName}
                    """,
            Set.of(USER_NAME, TASK_TITLE, TASK_TYPE, PROJECT_NAME)
    ),
    USER_ADDED_YOU_TO_PROJECT(
            "Вас добавили в новый проект",
            """
                    ${userName} добавил вас в проект ${projectName}
                    """,
            Set.of(USER_NAME, PROJECT_NAME)),
    USER_CREATED_COMMENT(
            "Пользователь оставил комментарий к задаче",
            """
                    В задаче ${taskSimpleId} ${taskTitle} появился новый комментарий от ${userName}
                    ${commentData}
                    """,
            Set.of(TASK_SIMPLE_ID, TASK_TITLE, USER_NAME, COMMENT_DATA)
    ),
    NEW_TASK_ASSIGNEE("У задачи изменился исполнитель",
            """
                    ${userName} изменил исполнителя задачи ${taskSimpleId} ${taskTitle} на ${newUserName}
                    """,
            Set.of(USER_NAME, TASK_SIMPLE_ID, TASK_TITLE, NEW_USER_NAME)),
    TASK_ASSIGNEE_REMOVED("У задачи удален исполнитель",
            """
                    ${userName} удалил исполнителя ${oldUserName} с задачи ${taskSimpleId} ${taskTitle}
                    """,
            Set.of(TASK_SIMPLE_ID, TASK_TITLE, USER_NAME, OLD_USER_NAME)),
    NEW_TASK_REVIEWER("У задачи изменился ревьюер",
            """
                    ${userName} изменил ревьюера задачи ${taskSimpleId} ${taskTitle} на ${newUserName}
                    """,
            Set.of(USER_NAME, TASK_SIMPLE_ID, TASK_TITLE, NEW_USER_NAME)),
    TASK_REVIEWER_REMOVED("У задачи удален ревьюер",
            """
                    ${userName} удалил ревьюера ${oldUserName} с задачи ${taskSimpleId} ${taskTitle}
                    """,
            Set.of(TASK_SIMPLE_ID, TASK_TITLE, USER_NAME, OLD_USER_NAME)),
    TASK_STATUS_UPDATED(
            "Обновлен статус задачи",
            "${userName} изменил статус задачи ${taskSimpleId} ${taskTitle} с ${oldTaskStatus} на ${newTaskStatus}",
            Set.of(USER_NAME, TASK_SIMPLE_ID, TASK_TITLE, OLD_TASK_STATUS, NEW_TASK_STATUS)
    ),
    USER_REMOVED_FROM_PROJECT(
            "Пользователь удален из проекта",
            "Пользователь ${userName} удалил из проекта ${projectName} пользователя ${oldUserName}",
            Set.of(USER_NAME, PROJECT_NAME, OLD_USER_NAME)
    ),
    TASK_MOVED_TO_NEW_SPRINT(
            "Задача перемещена в другой спринт",
            "Пользователь ${userName} перенес задачу ${taskSimpleId} ${taskTitle} в спринт ${sprintName}",
            Set.of(USER_NAME, TASK_SIMPLE_ID, TASK_TITLE, SPRINT_NAME)
    ),
    TASK_TIME_LOG_UPDATED(
            "Обновлено затраченное время на задачу",
            """
            Пользователь ${userName} обновил затраченное время на задачу ${taskSimpleId} ${taskTitle}.
            Текущее затраченное время - ${usedTime}
            """,
            Set.of(USER_NAME, TASK_SIMPLE_ID, TASK_TITLE, USED_TIME)
    );

    private final String title;
    private final String body;
    private final Set<AdditionalTemplateParam> additionalParams;

    @RequiredArgsConstructor
    @Getter
    public enum AdditionalTemplateParam {
        USER_NAME("userName"),
        TASK_TITLE("taskTitle"),
        TASK_SIMPLE_ID("taskSimpleId"),
        COMMENT_DATA("commentData"),
        TASK_TYPE("taskType"),
        PROJECT_NAME("projectName"),
        NEW_USER_NAME("newUserName"),
        OLD_USER_NAME("oldUserName"),
        OLD_TASK_STATUS("oldTaskStatus"),
        NEW_TASK_STATUS("newTaskStatus"),
        SPRINT_NAME("sprintName"),
        USED_TIME("usedTime");

        private final String key;
    }
}
