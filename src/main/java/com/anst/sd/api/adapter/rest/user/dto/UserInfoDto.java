package com.anst.sd.api.adapter.rest.user.dto;

import com.anst.sd.api.domain.security.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoDto {
    String firstName;
    String lastName;
    @NotNull
    String username;
    @NotNull
    String email;
    @NotNull
    @NotEmpty
    Set<Role> roles;
}
