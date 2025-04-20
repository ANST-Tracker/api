package com.anst.sd.api.adapter.rest.notification;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.rest.notification.dto.NotificationDtoMapper;
import com.anst.sd.api.adapter.rest.notification.dto.NotificationInfoDto;
import com.anst.sd.api.domain.notification.Notification;
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
        Notification notification = readFromFile("/NotificationDtoMapperTest/notification.json", Notification.class);

        NotificationInfoDto dto = mapper.mapToDto(notification);

        assertEqualsToFile("/NotificationDtoMapperTest/notificationDto.json", dto);
    }
}
