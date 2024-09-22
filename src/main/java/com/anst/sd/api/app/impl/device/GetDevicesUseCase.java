package com.anst.sd.api.app.impl.device;

import com.anst.sd.api.app.api.device.DeviceInfo;
import com.anst.sd.api.app.api.device.DeviceRepository;
import com.anst.sd.api.app.api.device.GetDevicesInbound;
import com.anst.sd.api.domain.security.Device;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua_parser.Client;
import ua_parser.Parser;

import java.net.InetAddress;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetDevicesUseCase implements GetDevicesInbound {
    private static final Parser PARSER = new Parser();

    private final DeviceRepository deviceRepository;
    private final DatabaseReader databaseReader;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceInfo> get(Long userId) {
        log.info("Getting devices for user {}", userId);
        List<Device> devices = deviceRepository.findByUserId(userId);
        return devices.stream()
            .map(this::getDeviceInfo)
            .toList();
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private DeviceInfo getDeviceInfo(Device device) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setLastLogin(device.getLastLogin());
        deviceInfo.setIpAddress(device.getIp());
        deviceInfo.setDeviceName(getDeviceName(device.getUserAgent()));
        deviceInfo.setLocationName(getDeviceLocation(device));
        return deviceInfo;
    }

    private String getDeviceName(String userAgent) {
        Client client = PARSER.parse(userAgent);
        return client.userAgent.family + " " +
            client.userAgent.major + "." +
            client.userAgent.minor + " - " +
            client.os.family + " " +
            client.os.major + "." +
            client.os.minor;
    }

    private String getDeviceLocation(Device device) {
        try {
            InetAddress ipAddress = InetAddress.getByName(device.getIp());
            CityResponse cityResponse = databaseReader.city(ipAddress);

            return String.format("%s, %s", cityResponse.getCountry().getName(),
                cityResponse.getCity().getName());
        } catch (Exception e) {
            log.warn("Error while getting device {} location by ip {}",
                device.getId(), device.getIp());
            return null;
        }
    }
}