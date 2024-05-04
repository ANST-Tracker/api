package com.anst.sd.api.app.api;

/**
 * Сервис недоступен
 */
public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(Exception e) {
        super(e);
    }
}
