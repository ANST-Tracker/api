package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.device.DeviceRepository;
import com.anst.sd.api.domain.security.Device;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.JwtResponse;
import com.anst.sd.api.security.app.impl.JwtService;
import com.anst.sd.api.security.domain.ERole;
import com.anst.sd.api.security.domain.JwtAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateTokensDelegate {
  private final DeviceRepository deviceRepository;
  private final JwtService jwtService;

  public JwtResponse generate(User user) {
    var curDevice = deviceRepository.save(Device.createDevice(user));
    var tokens = jwtService.generateAccessRefreshTokens(
            user.getUsername(), user.getId(), curDevice.getId(), ERole.USER);
    curDevice.setToken(tokens.getRefreshToken());
    fillDeviceInfo(curDevice);
    deviceRepository.save(curDevice);
    return tokens;
  }

  // ===================================================================================================================
  // = Implementation
  // ===================================================================================================================

  private void fillDeviceInfo(Device device) {
    JwtAuth jwtAuth = jwtService.getJwtAuth();
    device.setIp(jwtAuth.getRemoteAddress());
    device.setUserAgent(jwtAuth.getUserAgent());
  }
}
