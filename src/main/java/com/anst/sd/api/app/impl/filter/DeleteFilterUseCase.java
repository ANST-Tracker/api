package com.anst.sd.api.app.impl.filter;

import com.anst.sd.api.app.api.filter.DeleteFilterInbound;
import com.anst.sd.api.app.api.filter.FilterRepository;
import com.anst.sd.api.domain.filter.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteFilterUseCase implements DeleteFilterInbound {
    private final FilterRepository filterRepository;

    @Override
    @Transactional
    public Filter delete(String filterId, UUID userId) {
        log.info("Deleting filter {} for user {}", filterId, userId);
        Filter filter = filterRepository.findByIdAndUserId(filterId, userId);
        filterRepository.delete(filter);
        return filter;
    }
}
