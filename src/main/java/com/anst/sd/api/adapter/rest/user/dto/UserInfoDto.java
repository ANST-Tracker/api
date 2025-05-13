package com.anst.sd.api.adapter.rest.user.dto;

import com.anst.sd.api.domain.user.Position;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoDto {
    @NotNull
    UUID id;
    @NotNull
    String firstName;
    @NotNull
    String lastName;
    @NotNull
    String telegramId;
    @NotNull
    String departmentName;
    @NotNull
    Position position;
}
