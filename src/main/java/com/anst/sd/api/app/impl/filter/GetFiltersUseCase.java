package com.anst.sd.api.app.impl.filter;

import com.anst.sd.api.app.api.filter.FilterRepository;
import com.anst.sd.api.app.api.filter.GetFiltersInbound;
import com.anst.sd.api.domain.filter.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetFiltersUseCase implements GetFiltersInbound {
    private final FilterRepository filterRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Filter> get(UUID userId, UUID projectId) {
        log.info("Getting filters by user id {} or project id {}", userId, projectId);
        return filterRepository.findAllByProjectOrUserId(userId, projectId);
    }
}
