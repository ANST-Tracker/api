package com.anst.sd.api.app.api.device;

import com.anst.sd.api.domain.security.Device;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository {
    Optional<Device> findByDeviceToken(UUID token);

    Optional<Device> findById(Long id);

    Device save(Device device);
}
