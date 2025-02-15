package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.project.ProjectRepository;
import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.CreateAbstractTaskInBound;
import com.anst.sd.api.app.api.user.UserRepository;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateAbstractTaskUseCase implements CreateAbstractTaskInBound {
    private final AbstractTaskRepository abstractTaskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public AbstractTask create(UUID userId, AbstractTask task) {
        log.info("Creating task with userId {}", userId);
        User creator = userRepository.getById(userId);
        Project project = projectRepository.getById(task.getProject().getId());
        BigDecimal orderNumber = abstractTaskRepository.findNextOrderNumber(task.getId());
        task.setProject(project);
        task.setSimpleId(SimpleIdGenerationDelegate.idGenerator(task));
        task.setOrderNumber(orderNumber.add(BigDecimal.ONE));
        task.setCreator(creator);
        return abstractTaskRepository.save(task);
    }
}
