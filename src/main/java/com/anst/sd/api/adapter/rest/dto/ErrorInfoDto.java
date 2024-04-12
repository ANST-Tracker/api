package com.anst.sd.api.adapter.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ErrorInfoDto {
    private Long timestamp;
    private String message;
    private ErrorType type;

    public static ErrorInfoDto createErrorInfo(Exception e) {
        return ErrorInfoDto.builder()
                .message(e.getMessage())
                .timestamp(Instant.now().toEpochMilli())
                .build();
    }

    public enum ErrorType {
        CLIENT,
        VALIDATION,
        AUTH,
        SERVER;

        @Override
        public String toString() {
            return this.name();
        }
    }
}
