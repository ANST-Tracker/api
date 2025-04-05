package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.app.api.task.AbstractTaskRepository;
import com.anst.sd.api.app.api.task.FindTasksByFilterInbound;
import com.anst.sd.api.domain.filter.Filter;
import com.anst.sd.api.domain.task.AbstractTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindTasksByFilterUseCase implements FindTasksByFilterInbound {
    private final AbstractTaskRepository abstractTaskRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AbstractTask> find(Filter filter) {
        log.info("Searching tasks by filter {}", filter);
        return abstractTaskRepository.findByFilter(filter);
    }
}
