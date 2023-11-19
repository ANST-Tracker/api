package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.ClientException;
import com.anst.sd.api.app.api.RoleRepository;
import com.anst.sd.api.app.api.ServerException;
import com.anst.sd.api.app.api.security.SignupRequest;
import com.anst.sd.api.app.api.user.RegisterUserInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.Role;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.security.ERole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static com.anst.sd.api.app.api.ClientErrorMessages.EMAIL_ALREADY_TAKEN;
import static com.anst.sd.api.app.api.ClientErrorMessages.USERNAME_ALREADY_TAKEN;
import static com.anst.sd.api.app.api.ErrorMessages.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserUseCase implements RegisterUserInBound {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ClientException(USERNAME_ALREADY_TAKEN);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ClientException(EMAIL_ALREADY_TAKEN);
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository
                .findByName(ERole.USER)
                .orElseThrow(() -> new ServerException(INTERNAL_SERVER_ERROR));
        roles.add(userRole);
        user.setRoles(roles);
        return userRepository.save(user);
    }
}
