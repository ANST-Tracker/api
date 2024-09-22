package com.anst.sd.api.app.impl.tag;

import com.anst.sd.api.app.api.tag.GetTagsInBound;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTagsUseCase implements GetTagsInBound {
    private final TagRepository tagRepository;

    @Override
    public List<Tag> get(Long userId) {
        return tagRepository.findAllByUserId(userId);
    }
}
