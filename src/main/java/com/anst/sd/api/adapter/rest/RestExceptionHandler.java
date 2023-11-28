package com.anst.sd.api.adapter.rest;

import com.anst.sd.api.adapter.rest.dto.ErrorInfoDto;
import com.anst.sd.api.security.AuthException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

import static com.anst.sd.api.app.api.ErrorMessages.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Nullable
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            @Nullable Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request
    ) {
        logger.warn(ex.getMessage(), ex);
        var errorInfo = new ErrorInfoDto(
                Instant.now().toEpochMilli(),
                INTERNAL_SERVER_ERROR,
                ErrorInfoDto.ErrorType.SERVER);
        return super.handleExceptionInternal(ex, errorInfo, headers, statusCode, request);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleAuthException(AuthException ex) {
        logger.warn(ex.getMessage(), ex);
        var errorInfo = new ErrorInfoDto(
                ex.getTimestamp(),
                ex.getMessage(),
                ErrorInfoDto.ErrorType.AUTH);
        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn(ex.getMessage(), ex);
        var errorInfo = new ErrorInfoDto(
                Instant.now().toEpochMilli(),
                ex.getMessage(),
                ErrorInfoDto.ErrorType.CLIENT);
        return new ResponseEntity<>(errorInfo, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        logger.warn(ex.getMessage(), ex);
        var errorInfo = new ErrorInfoDto(
                Instant.now().toEpochMilli(),
                ex.getMessage(),
                ErrorInfoDto.ErrorType.SERVER);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}