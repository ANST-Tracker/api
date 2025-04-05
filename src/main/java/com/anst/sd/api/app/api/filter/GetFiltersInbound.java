package com.anst.sd.api.app.api.filter;

import com.anst.sd.api.domain.filter.Filter;

import java.util.List;
import java.util.UUID;

public interface GetFiltersInbound {
    List<Filter> get(UUID userId, UUID projectId);
}
