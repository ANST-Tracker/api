package com.anst.sd.api.app.api.security;

import com.anst.sd.api.domain.security.Device;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository {
    Optional<Device> findById(UUID id);

    Device save(Device device);
}
