package com.anst.sd.api.adapter.rest.project.dto;

import com.anst.sd.api.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoDto {
    @NotNull
    UUID id;
    @NotBlank
    String name;
    String description;
    @NotNull
    User head;
    @NotBlank
    String key;
}
