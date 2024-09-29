package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.tag.TagNotFoundException;
import com.anst.sd.api.app.api.tag.TagRepository;
import com.anst.sd.api.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    private final TagJpaRepository tagJpaRepository;

    @Override
    public Tag save(Tag tag) {
        return tagJpaRepository.save(tag);
    }

    @Override
    public List<Tag> findAllByUserId(Long userId) {
        return tagJpaRepository.findAllByUserId(userId);
    }

    @Override
    public void delete(Long id) {
        tagJpaRepository.deleteById(id);
    }

    @Override
    public Tag findByIdAndUserId(Long id, Long userId) {
        return tagJpaRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TagNotFoundException(id, userId));
    }

    @Override
    public List<Tag> findAllByIds(List<Long> ids) {
        return tagJpaRepository.findAllById(ids);
    }

    @Override
    public boolean existsByNameAndUserId(String name, Long userId) {
        return tagJpaRepository.existsByNameAndUserId(name, userId);
    }
}
