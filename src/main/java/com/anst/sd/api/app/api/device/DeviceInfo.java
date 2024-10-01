package com.anst.sd.api.app.api.device;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceInfo {
  private Instant lastLogin;
  private String locationName;
  private String deviceName;
  private String ipAddress;
}
