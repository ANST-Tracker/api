package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.device.DeviceNotFoundException;
import com.anst.sd.api.app.api.device.DeviceRepository;
import com.anst.sd.api.domain.security.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeviceRepositoryImpl implements DeviceRepository {
    private final DeviceJpaRepository deviceJpaRepository;

    @Override
    public Optional<Device> findById(Long id) {
        return deviceJpaRepository.findById(id);
    }

    @Override
    public Device save(Device device) {
        return deviceJpaRepository.save(device);
    }

    @Override
    public List<Device> findByUserId(Long userId) {
        return deviceJpaRepository.findAllByUserId(userId);
    }

    @Override
    public Device getByIdAndUserId(Long id, Long userId) {
        return deviceJpaRepository.findFirstByIdAndUserId(id, userId)
            .orElseThrow(() -> new DeviceNotFoundException(id, userId));
    }

    @Override
    public void deleteAll(List<Device> devices) {
        deviceJpaRepository.deleteAll(devices);
    }
}
