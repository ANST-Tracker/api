package com.anst.sd.api.app.impl.usercode;

import com.anst.sd.api.app.api.usercode.SendUserCodeToTelegramInBound;
import com.anst.sd.api.app.api.usercode.UserCodeRepository;
import com.anst.sd.api.domain.user.UserCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SendUserCodeToTelegramToTelegram implements SendUserCodeToTelegramInBound {
    private final CodeGeneration codeGeneration;
    private final UserCodeRepository userCodeRepository;

    @Override
    @Transactional
    public UserCode create(String userId, String telegramId) {
        UserCode userCode = new UserCode();

        String code = codeGeneration.generate();

        userCode.setCode(code);
        userCode.setUserId(String.valueOf(userId));
        userCode.setTelegramId(String.valueOf(telegramId));

        sendToKafka(userCode);

        return userCodeRepository.save(userCode);
    }

    public void sendToKafka(UserCode userCode) {

    }
}
