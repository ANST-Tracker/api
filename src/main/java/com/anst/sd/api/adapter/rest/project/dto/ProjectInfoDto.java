package com.anst.sd.api.adapter.rest.project.dto;

import com.anst.sd.api.adapter.rest.tag.dto.TagInfoDto;
import com.anst.sd.api.adapter.rest.user.dto.UserInfoDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
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
    UUID headId;
    List<TagInfoDto> tags;
    List<UserInfoDto> users;
}
