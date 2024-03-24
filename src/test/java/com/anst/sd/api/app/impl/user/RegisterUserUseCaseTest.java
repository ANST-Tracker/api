package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.security.RoleRepository;
import com.anst.sd.api.app.api.user.RegisterUserException;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.impl.JwtService;
import com.anst.sd.api.security.domain.JwtAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RegisterUserUseCaseTest extends AbstractUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private JwtService jwtService;
    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        registerUserUseCase = new RegisterUserUseCase(userRepository, jwtService, passwordEncoder, roleRepository, projectRepository);
    }

    @Test
    void registerUser_failed_userAlreadyExists() {
        String telegramJwt = "testTelegramJwt";
        when(userRepository.existsByTelegramId(any())).thenReturn(true);
        when(userRepository.existsByUsername(any())).thenReturn(true);
        User user = createTestUser();
        user.setTelegramId(telegramJwt);
        when(jwtService.getJwtAuth()).thenReturn(JwtAuth.builder().telegramId(telegramJwt).build());

        assertThrows(RegisterUserException.class, () -> registerUserUseCase.register(user),
            """
                    Telegram id is already in use
                    Username is already taken
                    """);
    }
}
