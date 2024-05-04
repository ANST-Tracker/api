package com.anst.sd.api.adapter.telegram.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserCodeDto {
    String telegramId;
    String code;
}
