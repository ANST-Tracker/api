package com.anst.sd.api.app.impl.task.security;

import com.anst.sd.api.app.api.security.CheckCodeInbound;
import com.anst.sd.api.app.api.security.UserCodeRepository;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.security.UserCode;
import com.anst.sd.api.security.app.api.AuthException;
import com.anst.sd.api.security.app.impl.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckCodeUseCase implements CheckCodeInbound {
    private static final String EXPIRED_CODE = "Code is expired";
    private static final String WRONG_CODE_ERROR = "Wrong code";

    private final UserCodeRepository userCodeRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, noRollbackFor = AuthException.class)
    public String check(String telegramId, String code, String username) {
        if (username != null) {
            telegramId = userRepository.getByUsername(username).getTelegramId();
        }
        UserCode userCode = userCodeRepository.getByTelegramId(telegramId);
        validateUserCode(userCode, code);
        userCodeRepository.delete(userCode);
        return jwtService.generateTelegramIdAccessToken(telegramId);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateUserCode(UserCode userCode, String code) {
        if (userCode.getExpirationTime().isBefore(Instant.now())) {
            userCodeRepository.delete(userCode);
            throw new AuthException(EXPIRED_CODE);
        }
        if (!userCode.getCode().equals(code)) {
            log.error("Wrong code {} ({} was sent)", code, userCode.getCode());
            throw new AuthException(WRONG_CODE_ERROR);
        }
    }
}
