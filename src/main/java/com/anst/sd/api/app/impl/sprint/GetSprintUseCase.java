package com.anst.sd.api.app.impl.sprint;

import com.anst.sd.api.app.api.sprint.GetSprintInBound;
import com.anst.sd.api.app.api.sprint.SprintRepository;
import com.anst.sd.api.domain.sprint.Sprint;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class GetSprintUseCase implements GetSprintInBound {
    private final SprintRepository sprintRepository;

    @Override
    @Transactional(readOnly = true)
    public Sprint get(UUID sprintId, UUID userId) {
        log.info("Getting sprint information by id {}, by user {}", sprintId, userId);
        return sprintRepository.getById(sprintId);
    }
}
