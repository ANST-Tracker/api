package com.anst.sd.api.adapter.rest.users_projects.write.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddUserInProjectDto {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID projectId;
    private String permissionCode;
}
