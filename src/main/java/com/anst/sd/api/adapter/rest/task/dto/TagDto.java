package com.anst.sd.api.adapter.rest.task.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagDto {
    UUID id;
    String name;
}
