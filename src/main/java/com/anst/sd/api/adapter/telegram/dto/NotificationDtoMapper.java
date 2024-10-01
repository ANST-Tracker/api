package com.anst.sd.api.adapter.telegram.dto;

import com.anst.sd.api.domain.notification.PendingNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationDtoMapper {
  @Mapping(target = "taskName", source = "task.data")
  @Mapping(target = "deadline", source = "task.deadline")
  @Mapping(target = "projectName", source = "task.project.name")
  @Mapping(target = "telegramId", source = "task.project.user.telegramId")
  NotificationDto mapToDto(PendingNotification pendingNotification);
}
