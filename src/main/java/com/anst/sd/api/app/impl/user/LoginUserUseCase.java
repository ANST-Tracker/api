package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.user.LoginUserInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.AuthException;
import com.anst.sd.api.security.app.api.JwtResponse;
import com.anst.sd.api.security.app.impl.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.anst.sd.api.security.app.api.AuthErrorMessages.INVALID_PASSWORD;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUserUseCase implements LoginUserInBound {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final GenerateTokensDelegate generateTokensDelegate;

  @Override
  @Transactional
  public JwtResponse login(String username, String password) {
    log.info("Logging user with username {}", username);
    User user = userRepository.getByUsername(username);

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new AuthException(INVALID_PASSWORD);
    }

    String tokenTgId = jwtService.getJwtAuth().getTelegramId();
    if (!user.getTelegramId().equals(tokenTgId)) {
      throw new AuthException("Trying to login user with tgId %s with token for tgId %s".formatted(user.getTelegramId(), tokenTgId));
    }

    return generateTokensDelegate.generate(user);
  }
}
