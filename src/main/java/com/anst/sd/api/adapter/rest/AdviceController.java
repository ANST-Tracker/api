package com.anst.sd.api.adapter.rest;

import com.anst.sd.api.app.api.ClientException;
import com.anst.sd.api.app.api.ErrorInfo;
import com.anst.sd.api.app.api.ServerException;
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
public class AdviceController extends ResponseEntityExceptionHandler {

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

        var errorInfo = new ErrorInfo(
                Instant.now().toEpochMilli(),
                INTERNAL_SERVER_ERROR);

        return super.handleExceptionInternal(ex, errorInfo, headers, statusCode, request);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<Object> handleClientException(ClientException ex) {
        var errorInfo = new ErrorInfo(
                ex.getTimestamp(),
                ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleAuthException(AuthException ex) {
        var errorInfo = new ErrorInfo(
                ex.getTimestamp(),
                ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleServerException(ServerException ex) {
        var errorInfo = new ErrorInfo(
                ex.getTimestamp(),
                ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        var errorInfo = new ErrorInfo(
                Instant.now().toEpochMilli(),
                ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        var errorInfo = new ErrorInfo(
                Instant.now().toEpochMilli(),
                ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
}