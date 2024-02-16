package com.anst.sd.api.app.impl.usercode;

import com.anst.sd.api.app.api.usercode.SendUserCodeToTelegramInBound;
import com.anst.sd.api.app.api.usercode.UserCodeRepository;
import com.anst.sd.api.domain.user.UserCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
public class SendUserCodeToTelegramDelegate implements SendUserCodeToTelegramInBound {
    private final CodeGenerationDelegate codeGenerationDelegate;
    private final UserCodeRepository userCodeRepository;

    @Override
    @Transactional
    public UserCode create(String userId, String telegramId) {
        UserCode userCode = new UserCode();

        String code = codeGenerationDelegate.generate();

        userCode.setCode(code);
        userCode.setUserId(String.valueOf(userId));
        userCode.setTelegramId(String.valueOf(telegramId));

        log.info("Entity has been sent to Kafka");
        return userCodeRepository.save(userCode);
    }
}
