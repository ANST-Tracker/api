package com.anst.sd.api.app.api.tag;

import com.anst.sd.api.domain.tag.Tag;

import java.util.List;
import java.util.UUID;

public interface TagRepository {
    Tag findById(UUID tagId);

    List<Tag> findAllByIdIn(List<UUID> ids);
}
