package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.tag.TagNotFoundException;
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
    public Tag save(Tag tag) {
        return tagJpaRepository.save(tag);
    }

    @Override
    public Tag findById(UUID id) {
        return tagJpaRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
    }

    @Override
    public List<Tag> findAllByProjectId(UUID projectId) {
        return tagJpaRepository.findAllByProjectId(projectId);
    }

    @Override
    public void delete(UUID id) {
        tagJpaRepository.deleteById(id);
    }

    @Override
    public List<Tag> findAllByIds(List<UUID> ids) {
        return tagJpaRepository.findAllById(ids);
    }

    @Override
    public boolean existsByNameAndProjectId(String name, UUID projectId) {
        return tagJpaRepository.existsByNameAndProjectId(name, projectId);
    }

    @Override
    public Tag findByNameAndProjectId(String name, UUID projectId) {
        return tagJpaRepository.findByNameAndProjectId(name, projectId)
                .orElseThrow(() -> new TagNotFoundException(name, projectId));
    }

    @Override
    public List<Tag> findAllByIdInAndProjectId(List<UUID> ids, UUID projectId) {
        return tagJpaRepository.findAllByIdInAndProjectId(ids, projectId);
    }
}
