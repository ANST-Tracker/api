package com.anst.sd.api.app.impl.filter;

import com.anst.sd.api.app.api.filter.CreateFilterInbound;
import com.anst.sd.api.app.api.filter.FilterRepository;
import com.anst.sd.api.app.api.filter.FilterValidationException;
import com.anst.sd.api.app.api.project.ProjectNotFoundException;
import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.domain.filter.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateFilterUseCase implements CreateFilterInbound {
    private final FilterRepository filterRepository;
    private final ProjectRepository projectRepository;
    private final SimpleFilterFieldsValidator simpleFilterFieldsValidator;

    @Override
    @Transactional
    public Filter create(Filter filter, UUID userId) {
        log.info("Creating filter {} for user {}", filter, userId);
        if (filter.getProjectId() != null) {
            validateProject(userId, filter.getProjectId());
        }
        validateFilter(filter);
        filter.setUserId(userId);
        return filterRepository.save(filter);
    }

    private void validateProject(UUID userId, UUID projectId) {
        if (!projectRepository.existsByIdAndUserId(userId, projectId)) {
            throw new ProjectNotFoundException(projectId, userId);
        }
    }

    private void validateFilter(Filter filter) {
        if (!simpleFilterFieldsValidator.isAnyFieldsNotEmpty(filter)) {
            log.error("Filter {} is not valid. All fields are empty", filter);
            throw new FilterValidationException();
        }
    }
}
