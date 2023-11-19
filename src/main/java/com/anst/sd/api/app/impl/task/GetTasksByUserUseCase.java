package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;
import com.anst.sd.api.adapter.rest.task.dto.TaskMapper;
import com.anst.sd.api.app.api.task.GetTasksByUserInBound;
import com.anst.sd.api.app.api.task.TaskRepository;
import com.anst.sd.api.domain.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTasksByUserUseCase implements GetTasksByUserInBound {
    private final TaskRepository taskRepository;
    @Value("${pageable.size}")
    private Integer pageSize;

    @Override
    public List<TaskInfo> getTasks(Long userId, Integer page) {
        if (page == null || page < 0) page = 0;
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("created").descending());
        var pageResponse = taskRepository.findTasksByUserId(userId, pageRequest);
        return pageResponse.stream()
                .sorted(Comparator.comparingInt(a -> TaskStatus.getPriority(a.getStatus())))
                .map(TaskMapper::toApi).toList();
    }
}
