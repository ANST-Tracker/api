package com.anst.sd.api.adapter.rest.user.dto;

import com.anst.sd.api.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class UserInfoResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Set<Role> roles;
}
