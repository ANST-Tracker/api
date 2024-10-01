package com.anst.sd.api.adapter.rest.tag.write.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTagDto {
  @NotBlank
  String name;
  @NotBlank
  String color;
}
