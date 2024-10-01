package com.anst.sd.api.app.impl.tag;

import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.app.api.tag.TagValidationException;
import com.anst.sd.api.app.api.tag.UpdateTagInBound;
import com.anst.sd.api.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateTagUseCase implements UpdateTagInBound {
  private final TagRepository tagRepository;

  @Override
  @Transactional
  public Tag update(Long userId, Long tagId, Tag updated) {
    log.info("Updating tag with id {} and userId {}", tagId, userId);
    if (tagRepository.existsByNameAndUserId(updated.getName(), userId)) {
      throw new TagValidationException(tagId);
    }
    Tag tag = tagRepository.findByIdAndUserId(tagId, userId);
    tag.setColor(updated.getColor());
    tag.setName(updated.getName());
    return tagRepository.save(tag);
  }
}
