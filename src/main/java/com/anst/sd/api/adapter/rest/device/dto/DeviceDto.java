package com.anst.sd.api.adapter.rest.device.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceDto {
  private String lastLogin;
  private String locationName;
  private String deviceName;
  private String ipAddress;
}
