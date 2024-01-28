package com.anst.sd.api.adapter.rest.project.dto;

import com.anst.sd.api.domain.project.ProjectType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoDto {
    @NotNull
    Long id;
    @NotBlank
    String name;
    @NotNull
    ProjectType type;
}
