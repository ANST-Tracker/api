package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.app.api.device.DeviceRepository;
import com.anst.sd.api.domain.security.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeviceRepositoryImpl implements DeviceRepository {
    private final DeviceJpaRepository deviceJpaRepository;

    @Override
    public Optional<Device> findByDeviceToken(UUID token) {
        return deviceJpaRepository.findByDeviceToken(token);
    }

    @Override
    public Optional<Device> findById(Long id) {
        return deviceJpaRepository.findById(id);
    }

    @Override
    public Device save(Device device) {
        return deviceJpaRepository.save(device);
    }
}
