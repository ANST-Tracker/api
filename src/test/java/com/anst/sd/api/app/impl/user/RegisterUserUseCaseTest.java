package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.security.RoleRepository;
import com.anst.sd.api.app.api.user.RegisterUserException;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
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
    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        registerUserUseCase = new RegisterUserUseCase(userRepository, passwordEncoder, roleRepository, projectRepository);
    }

    @Test
    void registerUser_failed_userAlreadyExists() {
        when(userRepository.existsByTelegramId(any())).thenReturn(true);
        when(userRepository.existsByUsername(any())).thenReturn(true);
        User user = createTestUser();

        assertThrows(RegisterUserException.class, () -> registerUserUseCase.register(user),
            """
                    Telegram id is already in use
                    Username is already taken
                    """);
    }
}
