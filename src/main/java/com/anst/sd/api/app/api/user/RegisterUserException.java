package com.anst.sd.api.app.api.user;

public class RegisterUserException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Can't register user. Reason %s";

  public RegisterUserException(String reason) {
    super(ERROR_MESSAGE.formatted(reason));
  }
}
