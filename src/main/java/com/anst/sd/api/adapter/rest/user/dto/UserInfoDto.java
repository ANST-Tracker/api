package com.anst.sd.api.adapter.rest.user.dto;

import com.anst.sd.api.domain.user.Position;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoDto {
    UUID id;
    String firstName;
    String lastName;
    String telegramId;
    String departmentName;
    Position position;
}
