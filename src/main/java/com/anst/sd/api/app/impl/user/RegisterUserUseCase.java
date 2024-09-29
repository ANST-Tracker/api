package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.security.RoleNotFoundException;
import com.anst.sd.api.app.api.security.RoleRepository;
import com.anst.sd.api.app.api.user.RegisterUserException;
import com.anst.sd.api.app.api.user.RegisterUserInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.project.ProjectType;
import com.anst.sd.api.domain.security.Role;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.app.api.JwtResponse;
import com.anst.sd.api.security.app.impl.JwtService;
import com.anst.sd.api.security.domain.ERole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserUseCase implements RegisterUserInBound {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final ProjectRepository projectRepository;
    private final GenerateTokensDelegate generateTokensDelegate;

    @Override
    @Transactional
    public JwtResponse register(User user) {
        log.info("User registration processing for username {}", user.getUsername());
        validateUser(user);
        user.setPassword(encoder.encode(user.getPassword()));
        addUserRole(user);
        user = userRepository.save(user);
        createUserBucketProject(user);

        return generateTokensDelegate.generate(user);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void createUserBucketProject(User user) {
        Project project = new Project();
        project.setName("Bucket");
        project.setProjectType(ProjectType.BUCKET);
        project.setUser(user);
        projectRepository.save(project);
    }

    private void addUserRole(User user) {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new RoleNotFoundException(ERole.USER.name()));
        roles.add(userRole);
        user.setRoles(roles);
    }

    private void validateUser(User user) {
        List<String> errors = new ArrayList<>();
        if (userRepository.existsByUsername(user.getUsername())) {
            errors.add("Username is already taken");
        }
        if (userRepository.existsByTelegramId(user.getTelegramId())) {
            errors.add("Telegram account is already in use");
        }
        String telegramIdFromJwt = jwtService.getJwtAuth().getTelegramId();
        if (!telegramIdFromJwt.equals(user.getTelegramId())) {
            errors.add("Trying to register user with telegramId %s by token for another id %s"
                    .formatted(user.getTelegramId(), telegramIdFromJwt));
        }
        if (!errors.isEmpty()) {
            throw new RegisterUserException(StringUtils.join(errors, '\n'));
        }
    }
}
