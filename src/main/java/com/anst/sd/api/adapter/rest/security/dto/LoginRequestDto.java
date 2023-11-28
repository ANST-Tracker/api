package com.anst.sd.api.adapter.rest.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequestDto {
    @NotNull
    @NotBlank
    String username;
    @NotNull
    @NotBlank
    String password;
    @NotNull
    @NotBlank
    UUID deviceToken;
}
