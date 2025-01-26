package com.anst.sd.api.adapter.telegram;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.telegram.dto.NotificationDto;
import com.anst.sd.api.domain.notification.Notification;
import com.anst.sd.api.domain.notification.NotificationTemplate;
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
            USER_LOGIN.getKey(), "testUser",
            TASK_TITLE.getKey(), "Реализовать тестовую задачу",
            LINK.getKey(), "www.google.com"
        ));
        notification.setTemplate(NotificationTemplate.TASK_NEW_ASSIGNEE);
        notification.setRecipientTelegramId("testTelegramId");

        NotificationDto dto = mapper.execute(notification);

        assertEquals("Назначена новая задача", dto.getTitle());
        assertEquals("""
            testUser назначил на тебя задачу "Реализовать тестовую задачу"
            www.google.com
            """, dto.getBody());
        assertEquals("testTelegramId", dto.getTelegramId());
    }
}