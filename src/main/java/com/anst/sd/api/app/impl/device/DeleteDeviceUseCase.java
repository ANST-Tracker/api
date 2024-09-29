package com.anst.sd.api.app.impl.device;

import com.anst.sd.api.app.api.device.DeleteDeviceInbound;
import com.anst.sd.api.app.api.device.DeviceRepository;
import com.anst.sd.api.app.impl.security.DeleteSessionDelegate;
import com.anst.sd.api.domain.security.Device;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteDeviceUseCase implements DeleteDeviceInbound {
    private final DeleteSessionDelegate deleteSessionDelegate;
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Long delete(Long userId, Long deviceId) {
        log.info("Deleting device {} on user {}", deviceId, userId);
        Device device = deviceRepository.getByIdAndUserId(deviceId, userId);
        deleteSessionDelegate.removeSession(device);
        return deviceId;
    }
}
