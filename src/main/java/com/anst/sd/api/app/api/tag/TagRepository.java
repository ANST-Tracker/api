package com.anst.sd.api.app.api.tag;

import com.anst.sd.api.domain.tag.Tag;

import java.util.List;

public interface TagRepository {
    Tag save(Tag tag);
    Tag findByIdAndUserId(Long id, Long userId);
    List<Tag> findAllByUserId(Long userId);
    List<Tag> findAllByIds(List<Long> ids);
    void delete(Long id);
    boolean existsByNameAndUserId(String name, Long userId);
}
