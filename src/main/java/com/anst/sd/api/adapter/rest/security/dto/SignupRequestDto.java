package com.anst.sd.api.adapter.rest.security.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequestDto {
    @NotEmpty
    @NotNull
    String username;
    @NotEmpty
    @NotNull
    String firstName;
    @NotNull
    @NotEmpty
    String lastName;
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+")
    String email;
    @NotNull
    @NotEmpty String password;
}
