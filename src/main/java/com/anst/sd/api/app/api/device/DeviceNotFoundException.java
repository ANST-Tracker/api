package com.anst.sd.api.app.api.device;

public class DeviceNotFoundException extends RuntimeException {
  private static final String ERROR_MESSAGE_TOKEN = "Device was not found by id %d and userId %d";

  public DeviceNotFoundException(Long id, Long userId) {
    super(ERROR_MESSAGE_TOKEN.formatted(id, userId));
  }
}
