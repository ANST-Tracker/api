package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    private final TagJpaRepository tagJpaRepository;

    @Override
    public List<Tag> findAllByIdInAndProjectId(List<UUID> ids, UUID projectId) {
        return tagJpaRepository.findAllByIdInAndProjectId(ids, projectId);
    }
}
