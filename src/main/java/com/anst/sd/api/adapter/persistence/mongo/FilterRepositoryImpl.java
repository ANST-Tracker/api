package com.anst.sd.api.adapter.persistence.mongo;

import com.anst.sd.api.app.api.filter.FilterNotFoundException;
import com.anst.sd.api.app.api.filter.FilterRepository;
import com.anst.sd.api.domain.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FilterRepositoryImpl implements FilterRepository {
    private final FilterMongoRepository filterMongoRepository;

    @Override
    public Filter save(Filter filter) {
        return filterMongoRepository.save(filter);
    }

    @Override
    public void delete(Filter filter) {
        filterMongoRepository.delete(filter);
    }

    @Override
    public Filter findByIdAndUserId(String id, UUID userId) {
        return filterMongoRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new FilterNotFoundException(id));
    }

    @Override
    public List<Filter> findAllByProjectOrUserId(UUID userId, UUID projectId) {
        return filterMongoRepository.findByUserIdOrProjectId(userId, projectId);
    }
}
