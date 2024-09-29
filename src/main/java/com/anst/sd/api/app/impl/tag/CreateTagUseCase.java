package com.anst.sd.api.app.impl.tag;

import com.anst.sd.api.app.api.tag.CreateTagInBound;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.tag.TagValidationException;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTagUseCase implements CreateTagInBound {
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public Tag create(Tag tag, Long userId) {
        validateTag(tag.getName(), userId);
        User user = userRepository.getById(userId);
        tag.setUser(user);
        return tagRepository.save(tag);
    }

    private void validateTag(String name, Long userId) {
        if (tagRepository.existsByNameAndUserId(name, userId)) {
            throw new TagValidationException();
        }
    }
}
