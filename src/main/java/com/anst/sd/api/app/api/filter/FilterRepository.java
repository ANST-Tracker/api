package com.anst.sd.api.app.api.filter;

import com.anst.sd.api.domain.filter.Filter;

import java.util.List;
import java.util.UUID;

public interface FilterRepository {
    Filter save(Filter filter);

    void delete(Filter filter);

    Filter findByIdAndUserId(String id, UUID userId);

    List<Filter> findAllByProjectOrUserId(UUID userId, UUID projectId);
}
