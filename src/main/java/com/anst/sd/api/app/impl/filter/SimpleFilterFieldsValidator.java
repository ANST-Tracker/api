package com.anst.sd.api.app.impl.filter;

import com.anst.sd.api.app.api.filter.FilterValidationException;
import com.anst.sd.api.domain.filter.Filter;
import com.anst.sd.api.domain.filter.FilterPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class SimpleFilterFieldsValidator {
    public boolean isAnyFieldsNotEmpty(Filter filter) {
        if (filter.getPayload() == null) {
            return false;
        }
        FilterPayload payload = filter.getPayload();
        return Arrays.stream(payload.getClass().getDeclaredFields())
            .anyMatch(field -> {
                try {
                    field.setAccessible(true);
                    Object value = field.get(payload);
                    if (value instanceof List list) {
                        return !CollectionUtils.isEmpty(list);
                    } else {
                        return value != null;
                    }
                } catch (IllegalAccessException e) {
                    log.error("Error checking fields for filter {}", filter, e);
                    throw new FilterValidationException();
                } finally {
                    field.setAccessible(false);
                }
            });
    }
}
