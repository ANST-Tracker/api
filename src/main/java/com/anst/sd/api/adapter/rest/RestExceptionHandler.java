package com.anst.sd.api.adapter.rest;

import com.anst.sd.api.app.api.device.DeviceNotFoundException;
import com.anst.sd.api.app.api.project.ProjectNotFoundException;
import com.anst.sd.api.app.api.project.ProjectValidationException;
import com.anst.sd.api.app.api.security.CodeAlreadySentException;
import com.anst.sd.api.app.api.security.RoleNotFoundException;
import com.anst.sd.api.app.api.security.UserCodeNotFoundException;
import com.anst.sd.api.app.api.tag.TagNotFoundException;
import com.anst.sd.api.app.api.tag.TagValidationException;
import com.anst.sd.api.app.api.task.TaskNotFoundException;
import com.anst.sd.api.app.api.task.TaskValidationException;
import com.anst.sd.api.app.api.user.RegisterUserException;
import com.anst.sd.api.app.api.user.UserNotFoundException;
import com.anst.sd.api.security.app.api.AuthException;
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

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<Object> handle(AuthException ex) {
    logger.error(ex.getMessage(), ex);
    var errorInfo = createErrorInfo(AUTH);
    return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({TaskValidationException.class, ProjectValidationException.class, TagValidationException.class})
  public ResponseEntity<Object> handleValidation(Exception ex) {
    logger.error(ex.getMessage(), ex);
    var errorInfo = createErrorInfo(CLIENT);
    return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RegisterUserException.class)
  public ResponseEntity<Object> handle(RegisterUserException ex) {
    logger.error(ex.getMessage(), ex);
    var errorInfo = createErrorInfo(CLIENT);
    return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handle(AccessDeniedException ex) {
    logger.error(ex.getMessage(), ex);
    var errorInfo = createErrorInfo(AUTH);
    return new ResponseEntity<>(errorInfo, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handle(RuntimeException ex) {
    logger.error(ex.getMessage(), ex);
    var errorInfo = createErrorInfo(SERVER);
    return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(CodeAlreadySentException.class)
  public ResponseEntity<Object> handle(CodeAlreadySentException ex) {
    logger.error(ex.getMessage(), ex);
    var errorInfo = createErrorInfo(CLIENT);
    return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
          DeviceNotFoundException.class,
          RoleNotFoundException.class,
          TaskNotFoundException.class,
          TagNotFoundException.class,
          UserNotFoundException.class,
          ProjectNotFoundException.class,
          UserCodeNotFoundException.class})
  public ResponseEntity<Object> handleNotFound(Exception ex) {
    logger.error(ex.getMessage(), ex);
    var errorInfo = createErrorInfo(CLIENT);
    return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
  }
}