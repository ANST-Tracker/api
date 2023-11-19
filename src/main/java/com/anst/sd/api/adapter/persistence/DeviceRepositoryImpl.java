package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.app.api.DeviceRepository;
import com.anst.sd.api.domain.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeviceRepositoryImpl implements DeviceRepository {
    private final DeviceJpaRepositoryOutBound repository;

    @Override
    public Optional<Device> findByDeviceToken(UUID token) {
        return repository.findByDeviceToken(token);
    }

    @Override
    public Optional<Device> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Device save(Device device) {
        return repository.save(device);
    }
}
