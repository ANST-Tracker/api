package com.anst.sd.api.app.api;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ServerException extends RuntimeException {
    private Long timestamp;
    private ErrorInfo.ErrorType errorType;
    private String errorMessage;

    public ServerException(String message) {
        super(message);
        this.timestamp = Instant.now().toEpochMilli();
        this.errorType = ErrorInfo.ErrorType.SERVER;
    }
}
