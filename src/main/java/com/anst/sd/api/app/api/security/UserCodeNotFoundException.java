package com.anst.sd.api.app.api.security;

public class UserCodeNotFoundException extends RuntimeException {
  private static final String ERROR_MESSAGE = "User code for telegram id %s was not found";

  public UserCodeNotFoundException(String telegramId) {
    super(ERROR_MESSAGE.formatted(telegramId));
  }
}
