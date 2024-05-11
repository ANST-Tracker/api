package com.anst.sd.api.app.api;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(Exception e) {
        super(e);
    }
}
