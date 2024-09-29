package com.anst.sd.api.app.impl.security;

import com.anst.sd.api.app.api.device.DeviceRepository;
import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.security.Device;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.impl.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class DeleteSessionDelegate {
    private final DeviceRepository deviceRepository;
    private final JwtService jwtService;

    public void removeSession(Device device) {
        disableDevices(List.of(device));
    }

    public void removeAllSessions(User user) {
        List<Device> devices = deviceRepository.findByUserId(user.getId());
        disableDevices(devices);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void disableDevices(List<Device> devices) {
        List<Long> deviceIds = devices.stream()
            .map(DomainObject::getId)
            .toList();
        jwtService.disableAccessTokens(deviceIds);
        deviceRepository.deleteAll(devices);
    }
}
