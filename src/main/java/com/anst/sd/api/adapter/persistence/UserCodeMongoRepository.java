package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.domain.user.UserCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCodeMongoRepository extends MongoRepository<UserCode, String> {
}
