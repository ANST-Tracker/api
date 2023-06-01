package com.anst.sd.api.builder;

import com.anst.sd.api.model.dto.response.TaskInfo;
import com.anst.sd.api.model.dto.request.CreateTaskRequest;
import com.anst.sd.api.model.dto.request.UpdateTaskRequest;
import com.anst.sd.api.model.entity.Task;
import com.anst.sd.api.model.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    public static Task toModel(CreateTaskRequest request) {
        return Task.builder()
                .data(request.getData())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .status(TaskStatus.BACKLOG)
                .description(request.getDescription())
                .build();
    }

    public static Task toModel(UpdateTaskRequest request) {
        return Task.builder()
                .data(request.getData())
                .status(request.getStatus())
                .description(request.getDescription())
                .id(request.getId())
                .modified(LocalDateTime.now())
                .build();
    }

    public static TaskInfo toApi(Task task) {
        return TaskInfo.builder()
                .deadline(task.getDeadline())
                .id(task.getId())
                .description(task.getDescription())
                .status(task.getStatus())
                .data(task.getData())
                .build();
    }
}
