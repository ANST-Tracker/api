package com.anst.sd.api.adapter.telegram.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.domain.notification.PendingNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class NotificationDtoMapperTest extends AbstractUnitTest {
    private NotificationDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(NotificationDtoMapper.class);
    }

    @Test
    void mapToDto() {
        PendingNotification pendingNotification = readFromFile("/NotificationDtoMapperTest/notification.json",
            PendingNotification.class);

        NotificationDto dto = mapper.mapToDto(pendingNotification);

        assertEqualsToFile("/NotificationDtoMapperTest/notificationDto.json", dto);
    }
}