package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.app.api.usercode.UserCodeRepository;
import com.anst.sd.api.domain.user.UserCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCodeRepositoryImpl implements UserCodeRepository {
    private final UserCodeMongoRepository userCodeMongoRepository;

    @Override
    public UserCode save(UserCode userCode) {
        return userCodeMongoRepository.save(userCode);
    }
}
