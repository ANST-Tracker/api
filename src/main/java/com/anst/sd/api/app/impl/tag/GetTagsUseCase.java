package com.anst.sd.api.app.impl.tag;

import com.anst.sd.api.app.api.tag.GetTagsInBound;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTagsUseCase implements GetTagsInBound {
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public List<Tag> get(Long userId) {
        log.info("Getting tags by userId {}", userId);
        return tagRepository.findAllByUserId(userId);
    }
}
