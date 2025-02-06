package com.anst.sd.api.adapter.rest;

import com.anst.sd.api.app.api.user.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.anst.sd.api.adapter.rest.dto.ErrorInfoDto.ErrorType.*;
import static com.anst.sd.api.adapter.rest.dto.ErrorInfoDto.createErrorInfo;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Nullable
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            @Nullable Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request) {
        logger.error(ex.getMessage(), ex);
        var errorInfo = createErrorInfo(SERVER);
        return super.handleExceptionInternal(ex, errorInfo, headers, statusCode, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handle(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        var errorInfo = createErrorInfo(SERVER);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(Exception ex) {
        logger.error(ex.getMessage(), ex);
        var errorInfo = createErrorInfo(CLIENT);
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }
}