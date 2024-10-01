package com.anst.sd.api.app.impl.security;

import com.anst.sd.api.app.api.device.DeviceRepository;
import com.anst.sd.api.app.api.security.RefreshTokenInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.security.Device;
import com.anst.sd.api.security.app.api.AuthErrorMessages;
import com.anst.sd.api.security.app.api.AuthException;
import com.anst.sd.api.security.app.api.JwtResponse;
import com.anst.sd.api.security.app.impl.JwtService;
import com.anst.sd.api.security.domain.JwtAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase implements RefreshTokenInBound {
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final DeviceRepository deviceRepository;

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public JwtResponse refresh(String refreshToken) {
    log.info("Refreshing token {}", refreshToken);
    if (!jwtService.validateRefreshToken(refreshToken)) {
      throw new AuthException(AuthErrorMessages.INVALID_REFRESH_TOKEN);
    }

    var claims = jwtService.getRefreshClaims(refreshToken);
    var user = userRepository.getById(Long.parseLong(claims.getUserId()));
    var device = deviceRepository.findById(Long.parseLong(claims.getDeviceId()))
            .orElseThrow(() -> new AuthException(AuthErrorMessages.DEVICE_NOT_FOUND));
    var role = claims.getRole();

    if (!device.getToken().equals(refreshToken)) {
      throw new AuthException(AuthErrorMessages.INVALID_REFRESH_TOKEN);
    }

    if (!jwtService.validateAccessTokenLifetime(device.getId())) {
      throw new AuthException(AuthErrorMessages.SUSPICIOUS_ACTIVITY);
    }

    var tokens = jwtService.generateAccessRefreshTokens(user.getUsername(), user.getId(), device.getId(), role);
    device.setToken(tokens.getRefreshToken());
    fillDeviceInfo(device);
    deviceRepository.save(device);
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
