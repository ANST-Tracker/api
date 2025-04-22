package com.anst.sd.api.adapter.rest.notification;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.rest.notification.dto.NotificationInfoDto;
import com.anst.sd.api.adapter.telegram.NotificationDtoMapper;
import com.anst.sd.api.domain.notification.Notification;
import org.junit.jupiter.api.Test;

class NotificationDtoMapperTest extends AbstractUnitTest {
    private final NotificationDtoMapper mapper = new NotificationDtoMapper();

    @Test
    void mapToDto() {
        Notification notification = readFromFile("/NotificationDtoMapperTest/notification.json", Notification.class);

        NotificationInfoDto dto = mapper.mapToDto(notification);

        assertEqualsToFile("/NotificationDtoMapperTest/notificationDto.json", dto);
    }
}
