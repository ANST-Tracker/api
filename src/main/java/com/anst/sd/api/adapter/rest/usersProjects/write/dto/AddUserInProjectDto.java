package com.anst.sd.api.adapter.rest.usersProjects.write.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String permissionCode;
}
