package com.anst.sd.api.app.api.device;

import com.anst.sd.api.domain.security.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
  Optional<Device> findById(Long id);

  Device save(Device device);

  List<Device> findByUserId(Long userId);

  Device getByIdAndUserId(Long id, Long userId);

  void deleteAll(List<Device> devices);
}
