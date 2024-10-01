package com.anst.sd.api.app.api.device;

import java.util.List;

public interface GetDevicesInbound {
  List<DeviceInfo> get(Long userId);
}
