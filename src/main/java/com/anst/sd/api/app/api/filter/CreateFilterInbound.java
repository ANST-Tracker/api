package com.anst.sd.api.app.api.filter;

import com.anst.sd.api.domain.filter.Filter;

import java.util.UUID;

public interface CreateFilterInbound {
    Filter create(Filter filter, UUID userId);
}
