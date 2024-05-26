package com.anst.sd.api.app.api.project;

import com.anst.sd.api.domain.project.ProjectType;

public class ProjectNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE_BY_USER = "User with id %d doesn't have project with id %d";
    private static final String ERROR_MESSAGE_BY_TELEGRAM_USER_PROJECT_TYPE = "User with telegram id %s does not have project of type %s";

    public ProjectNotFoundException(Long id, Long userId) {
        super(ERROR_MESSAGE_BY_USER.formatted(userId, id));
    }

    public ProjectNotFoundException(ProjectType projectType, String telegramUserId) {
        super(ERROR_MESSAGE_BY_TELEGRAM_USER_PROJECT_TYPE.formatted(telegramUserId, projectType));
    }
}
