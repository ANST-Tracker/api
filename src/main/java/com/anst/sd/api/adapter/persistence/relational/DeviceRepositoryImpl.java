package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.security.DeviceRepository;
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
    public Optional<Device> findById(UUID id) {
        return deviceJpaRepository.findById(id);
    }

    @Override
    public Device save(Device device) {
        return deviceJpaRepository.save(device);
    }
}
