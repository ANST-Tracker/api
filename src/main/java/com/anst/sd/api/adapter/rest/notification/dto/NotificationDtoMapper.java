package com.anst.sd.api.adapter.rest.notification.dto;

import com.anst.sd.api.domain.notification.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationDtoMapper {
    NotificationInfoDto mapToDto(Notification notification);
}
