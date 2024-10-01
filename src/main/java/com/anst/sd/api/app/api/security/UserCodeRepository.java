package com.anst.sd.api.app.api.security;

import com.anst.sd.api.domain.security.UserCode;

import java.util.Optional;

public interface UserCodeRepository {
  UserCode save(UserCode userCode);

  Optional<UserCode> findByTelegramId(String telegramId);

  UserCode getByTelegramId(String telegramId);

  void delete(UserCode userCode);
}
