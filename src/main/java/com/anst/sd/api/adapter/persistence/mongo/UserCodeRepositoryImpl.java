package com.anst.sd.api.adapter.persistence.mongo;

import com.anst.sd.api.app.api.security.UserCodeNotFoundException;
import com.anst.sd.api.app.api.security.UserCodeRepository;
import com.anst.sd.api.domain.security.UserCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCodeRepositoryImpl implements UserCodeRepository {
  private final UserCodeMongoRepository userCodeMongoRepository;

  @Override
  public UserCode save(UserCode userCode) {
    return userCodeMongoRepository.save(userCode);
  }

  @Override
  public Optional<UserCode> findByTelegramId(String telegramId) {
    return userCodeMongoRepository.findUserCodeByTelegramId(telegramId);
  }

  @Override
  public UserCode getByTelegramId(String telegramId) {
    return userCodeMongoRepository.findUserCodeByTelegramId(telegramId)
            .orElseThrow(() -> new UserCodeNotFoundException(telegramId));
  }

  @Override
  public void delete(UserCode userCode) {
    userCodeMongoRepository.delete(userCode);
  }
}
