package com.anst.sd.api.app.impl.tag;

import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.tag.UpdateTagInBound;
import com.anst.sd.api.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTagUseCase implements UpdateTagInBound {
    private final TagRepository tagRepository;

    @Override
    public Tag update(Long userId, Long tagId, Tag updated) {
        Tag tag = tagRepository.findByIdAndUserId(tagId, userId);
        tag.setColor(updated.getColor());
        tag.setName(updated.getName());
        return tagRepository.save(tag);
    }
}
