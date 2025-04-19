package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.app.api.sprint.SprintRepository;
import com.anst.sd.api.domain.sprint.Sprint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SprintRepositoryImpl implements SprintRepository {
    private final SprintJpaRepository sprintJpaRepository;

    @Override
    public List<Sprint> getAllByProjectId(UUID projectId) {
        return sprintJpaRepository.findAllByProjectId(projectId);
    }
}
