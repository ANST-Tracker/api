package com.anst.sd.api.app.api.security;

public class CodeAlreadySentException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Code already sent to telegramId %s";

  public CodeAlreadySentException(String telegramId) {
    super(ERROR_MESSAGE.formatted(telegramId));
  }
}
