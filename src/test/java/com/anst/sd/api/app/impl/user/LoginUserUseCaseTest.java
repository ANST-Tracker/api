package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.AuthException;
import com.anst.sd.api.security.app.impl.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.anst.sd.api.security.app.api.AuthErrorMessages.INVALID_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LoginUserUseCaseTest extends AbstractUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private GenerateTokensDelegate generateTokensDelegate;
    private LoginUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new LoginUserUseCase(userRepository, passwordEncoder, jwtService, generateTokensDelegate);
    }

    @Test
    void loginUser_failed_wrongPassword() {
        User user = createTestUser();
        when(userRepository.getByUsername(any())).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(AuthException.class,
            () -> useCase.login("userName", "wrongPass"),
            INVALID_PASSWORD);
    }
}