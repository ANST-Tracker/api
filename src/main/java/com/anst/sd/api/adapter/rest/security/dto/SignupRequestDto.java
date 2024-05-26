package com.anst.sd.api.adapter.rest.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequestDto {
    @NotBlank
    String username;
    String firstName;
    String lastName;
    @NotBlank
    String telegramId;
    @NotBlank
    String password;
    @NotNull
    UUID deviceToken;
}
