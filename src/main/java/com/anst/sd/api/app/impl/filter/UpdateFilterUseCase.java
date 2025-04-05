package com.anst.sd.api.app.impl.filter;

import com.anst.sd.api.app.api.filter.FilterRepository;
import com.anst.sd.api.app.api.filter.FilterValidationException;
import com.anst.sd.api.app.api.filter.UpdateFilterInbound;
import com.anst.sd.api.domain.filter.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateFilterUseCase implements UpdateFilterInbound {
    private final FilterRepository filterRepository;
    private final SimpleFilterFieldsValidator filterFieldsValidator;

    @Override
    @Transactional
    public Filter update(String filterId, Filter filter, UUID userId) {
        log.info("Updating filter {} for user {}. New value {}", filterId, userId, filter);
        Filter original = filterRepository.findByIdAndUserId(filterId, userId);
        if (!filterFieldsValidator.isAnyFieldsNotEmpty(filter)) {
            log.error("New filter values are empty");
            throw new FilterValidationException(filterId);
        }
        original.setName(filter.getName());
        original.setPayload(filter.getPayload());
        return filterRepository.save(original);
    }
}
