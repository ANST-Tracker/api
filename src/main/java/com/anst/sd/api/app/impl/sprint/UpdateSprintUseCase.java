package com.anst.sd.api.app.impl.sprint;

import com.anst.sd.api.app.api.sprint.SprintRepository;
import com.anst.sd.api.app.api.sprint.SprintValidationException;
import com.anst.sd.api.app.api.sprint.UpdateSprintInBound;
import com.anst.sd.api.domain.sprint.Sprint;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateSprintUseCase implements UpdateSprintInBound {
    private final SprintRepository sprintRepository;
    private final SprintValidationDelegate sprintValidationDelegate;

    @Override
    @Transactional
    public Sprint update(UUID userId, UUID sprintId, Sprint updated) {
        log.info("Updating sprint with id {} and userId {}", sprintId, userId);
        Sprint original = sprintRepository.getById(sprintId);
        sprintValidationDelegate.validateUserHasAccessToProject(userId, original.getProject().getId());
        verifyDates(updated.getStartDate(), updated.getEndDate());
        original.setName(updated.getName());
        original.setDescription(updated.getDescription());
        original.setIsActive(updated.getIsActive());
        original.setStartDate(updated.getStartDate());
        original.setEndDate(updated.getEndDate());
        return sprintRepository.save(original);
    }

    private void verifyDates(java.time.LocalDate start, java.time.LocalDate end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new SprintValidationException("startDate must not be after endDate");
        }
    }
}
