package com.anst.sd.api.app.api;


import com.anst.sd.api.adapter.rest.dto.ErrorInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AuthException extends RuntimeException {
    private Long timestamp;
    private ErrorInfo.ErrorType errorType;
    private String errorMessage;

    public AuthException(String message) {
        super(message);
        this.timestamp = Instant.now().toEpochMilli();
        this.errorType = ErrorInfo.ErrorType.AUTH;
    }
}
