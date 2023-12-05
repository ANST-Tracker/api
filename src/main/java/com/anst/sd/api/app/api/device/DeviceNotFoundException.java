package com.anst.sd.api.app.api.device;

public class DeviceNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE_TOKEN = "Device was not found for token %s";

    public DeviceNotFoundException(String deviceToken) {
        super(ERROR_MESSAGE_TOKEN.formatted(deviceToken));
    }
}
