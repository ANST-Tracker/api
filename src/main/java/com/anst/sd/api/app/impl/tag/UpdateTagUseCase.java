package com.anst.sd.api.app.impl.tag;

import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.tag.TagValidationException;
import com.anst.sd.api.app.api.tag.UpdateTagInBound;
import com.anst.sd.api.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateTagUseCase implements UpdateTagInBound {
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public Tag update(Long userId, Long tagId, Tag updated) {
        validateTag(updated.getName(), userId, tagId);
        Tag tag = tagRepository.findByIdAndUserId(tagId, userId);
        tag.setColor(updated.getColor());
        tag.setName(updated.getName());
        return tagRepository.save(tag);
    }

    private void validateTag(String name, Long userId, Long tagId) {
        if (tagRepository.existsByNameAndUserId(name, userId)) {
            throw new TagValidationException(tagId);
        }
    }
}
