package com.anst.sd.api.adapter.persistence.mongo;

import com.anst.sd.api.domain.security.UserCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCodeMongoRepository extends MongoRepository<UserCode, String> {
  Optional<UserCode> findUserCodeByTelegramId(String telegramId);
}
