package com.anst.sd.api.app.api.tag;

import com.anst.sd.api.domain.tag.Tag;

import java.util.List;
import java.util.UUID;

public interface TagRepository {
    List<Tag> findAllByIdInAndProjectId(List<UUID> ids, UUID projectId);
}
