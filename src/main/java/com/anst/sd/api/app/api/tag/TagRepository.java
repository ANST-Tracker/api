package com.anst.sd.api.app.api.tag;

import com.anst.sd.api.domain.tag.Tag;

import java.util.List;
import java.util.UUID;

public interface TagRepository {
    Tag save(Tag tag);

    Tag findById(UUID id);

    List<Tag> findAllByProjectId(UUID projectId);

    List<Tag> findAllByIds(List<UUID> ids);

    void delete(UUID id);

    boolean existsByNameAndProjectId(String name, UUID userId);

    Tag findByNameAndProjectId(String name, UUID projectId);

    List<Tag> findAllByIdInAndProjectId(List<UUID> ids, UUID projectId);
}
