package com.anst.sd.api.adapter.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorInfoDto {
    private Long timestamp;
    private String message;
    private ErrorType type;

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
