package com.anst.sd.api.app.impl.user;

import com.anst.sd.api.app.api.user.GetUsersAutocompleteInbound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetUsersAutocompleteUseCase implements GetUsersAutocompleteInbound {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<User> get(String nameFragment) {
        log.info("Getting users by name fragment: {}", nameFragment);
        return userRepository.findByNameFragment(nameFragment.toLowerCase());
    }
}
