package com.anst.sd.api.app.impl.usercode;

import com.anst.sd.api.app.api.usercode.CreateUserCodeInBound;
import com.anst.sd.api.app.api.usercode.UserCodeRepository;
import com.anst.sd.api.domain.user.UserCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
public class CreateUserCodeDelegate implements CreateUserCodeInBound {
    private final CodeGenerationDelegate codeGenerationDelegate;
    private final UserCodeRepository userCodeRepository;

    @Override
    @Transactional
    public UserCode create(String userId, String telegramId) {
        log.info("Create user code processing started");

        UserCode userCode = new UserCode()
                .setCode(codeGenerationDelegate.generate())
                .setUserId(userId)
                .setTelegramId(telegramId);
        return userCodeRepository.save(userCode);
    }
}