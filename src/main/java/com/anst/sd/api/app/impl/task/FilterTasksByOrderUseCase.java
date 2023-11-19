package com.anst.sd.api.app.impl.task;

import com.anst.sd.api.adapter.rest.task.dto.FilterRequest;
import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;
import com.anst.sd.api.adapter.rest.task.dto.TaskMapper;
import com.anst.sd.api.app.api.task.FilterTasksByOrderInBound;
import com.anst.sd.api.app.api.task.FilterTasksBySortOrderInBound;
import com.anst.sd.api.domain.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class FilterTasksByOrderUseCase implements FilterTasksByOrderInBound {
    private final FilterTasksBySortOrderInBound filterTasksBySortOrderInBound;
    @Override
    public List<TaskInfo> filter(FilterRequest filterRequest, List<Task> tasks) {
        if (filterRequest.getOrders() != null)
            for (var el : filterRequest.getOrders())
                tasks = filterTasksBySortOrderInBound.filterInternal(tasks, el);

        return tasks.stream().map(TaskMapper::toApi).toList();
    }
}
