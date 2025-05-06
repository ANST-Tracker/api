package com.anst.sd.api.adapter.telegram;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.telegram.dto.NotificationDto;
import com.anst.sd.api.domain.notification.Notification;
import com.anst.sd.api.domain.notification.NotificationTemplate;
import com.anst.sd.api.domain.task.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.anst.sd.api.domain.notification.NotificationTemplate.AdditionalTemplateParam.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationDtoMapperTest extends AbstractUnitTest {
    private NotificationDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NotificationDtoMapper();
    }

    @Test
    void execute() {
        Notification notification = new Notification();
        notification.setParams(Map.of(
                USER_NAME.getKey(), "testUser",
                TASK_TITLE.getKey(), "Реализовать тестовую задачу",
                TASK_TYPE.getKey(), TaskType.EPIC.getValue(),
                PROJECT_NAME.getKey(), "project"
        ));
        notification.setTemplate(NotificationTemplate.NEW_TASK_IN_PROJECT);
        notification.setRecipientTelegramId("testTelegramId");

        NotificationDto dto = mapper.execute(notification);

        assertEquals("Новая задача в проекте", dto.getTitle());
        assertEquals("""
                testUser создал задачу Реализовать тестовую задачу с типом Эпик стори в проекте project
                """, dto.getBody());
        assertEquals("testTelegramId", dto.getTelegramId());
    }
}