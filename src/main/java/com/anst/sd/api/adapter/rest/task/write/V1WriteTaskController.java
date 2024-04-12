package com.anst.sd.api.adapter.rest.task.write;

import com.anst.sd.api.adapter.rest.dto.IdResponseDto;
import com.anst.sd.api.adapter.rest.task.dto.TaskDomainMapper;
import com.anst.sd.api.adapter.rest.task.dto.TaskDtoMapper;
import com.anst.sd.api.adapter.rest.task.dto.TaskInfoDto;
import com.anst.sd.api.adapter.rest.task.write.dto.CreateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskDto;
import com.anst.sd.api.app.api.task.CreateTaskInBound;
import com.anst.sd.api.app.api.task.DeleteTaskInBound;
import com.anst.sd.api.app.api.task.TaskValidationException;
import com.anst.sd.api.app.api.task.UpdateTaskInBound;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/task")
@RestController
@RequiredArgsConstructor
public class V1WriteTaskController {
    private final UpdateTaskInBound updateTaskInBound;
    private final DeleteTaskInBound deleteTaskInBound;
    private final JwtService jwtService;
    private final CreateTaskInBound createTaskInBound;
    private final TaskDtoMapper taskDtoMapper;
    private final TaskDomainMapper taskDomainMapper;

    @Operation(
            summary = "Create a new task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task created successfully",
                            useReturnTypeSchema = true)
            })
    @PostMapping("/{projectId}")
    public ResponseEntity<TaskInfoDto> createTask(
            @Valid @RequestBody CreateTaskDto request,
            @PathVariable Long projectId,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new TaskValidationException();
        }
        Task task = taskDomainMapper.mapToDomain(request);
        Task result = createTaskInBound.create(jwtService.getJwtAuth().getUserId(), projectId, task);
        return ResponseEntity.ok(taskDtoMapper.mapToDto(result));
    }

    @Operation(
            summary = "Update task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task updated successfully",
                            useReturnTypeSchema = true)
            })
    @PutMapping("/{id}")
    public ResponseEntity<IdResponseDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskDto request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new TaskValidationException(id);
        }
        Task task = taskDomainMapper.mapToDomain(request);
        Task result = updateTaskInBound.update(jwtService.getJwtAuth().getUserId(), id, task);
        return ResponseEntity.ok(new IdResponseDto(result.getId()));
    }

    @Operation(
            summary = "Delete task by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task deleted successfully",
                            useReturnTypeSchema = true)
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<IdResponseDto> deleteTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        Task task = deleteTaskInBound.delete(jwtService.getJwtAuth().getUserId(), id);
        return ResponseEntity.ok(new IdResponseDto(task.getId()));
    }
}
