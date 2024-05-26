package com.anst.sd.api.app.impl.security;

import com.anst.sd.api.app.api.security.CodeAlreadySentException;
import com.anst.sd.api.app.api.security.SendCodeInbound;
import com.anst.sd.api.app.api.security.SendCodeOutbound;
import com.anst.sd.api.app.api.security.UserCodeRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.security.UserCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendCodeUseCase implements SendCodeInbound {
    @Value("${security.code.expiration-time}")
    private Long codeTimeToLive;
    private final UserCodeRepository userCodeRepository;
    private final SendCodeOutbound sendCodeOutbound;
    private final UserRepository userRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String send(String telegramId, String username) {
        log.info("Create user code processing started for telegramId {} username {}", telegramId, username);
        if (username != null) {
            telegramId = userRepository.getByUsername(username).getTelegramId();
        }
        Optional<UserCode> userCodeOptional = userCodeRepository.findByTelegramId(telegramId);
        UserCode userCode;
        if (userCodeOptional.isPresent()) {
            userCode = userCodeOptional.get();
            validateToSendNew(userCode);
        } else {
            userCode = new UserCode(telegramId);
        }
        userCode.setExpirationTime(Instant.now().plusSeconds(codeTimeToLive));
        String authCode = CodeGenerationDelegate.generate();
        userCode.setCode(authCode);

        sendCodeOutbound.send(userCode);
        userCodeRepository.save(userCode);
        return authCode;
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateToSendNew(UserCode userCode) {
        if (userCode.getExpirationTime().isAfter(Instant.now())) {
            throw new CodeAlreadySentException(userCode.getTelegramId());
        }
    }
}
