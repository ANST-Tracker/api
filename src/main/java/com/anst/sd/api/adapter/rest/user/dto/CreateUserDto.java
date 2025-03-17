package com.anst.sd.api.adapter.rest.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserDto {
    @NotBlank
    String username;
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @NotBlank
    String email;
    @NotBlank
    String telegramId;
    @NotBlank
    String password;
    @NotBlank
    String position;
    @NotBlank
    String departmentName;
    @NotBlank
    LocalDate registrationDate;
    @NotBlank
    Integer timeZone;
}
