package com.anst.sd.api.adapter.rest.task.write;

import com.anst.sd.api.adapter.rest.dto.IdResponseDto;
import com.anst.sd.api.adapter.rest.task.dto.TaskDomainMapper;
import com.anst.sd.api.adapter.rest.task.dto.TaskDtoMapper;
import com.anst.sd.api.adapter.rest.task.dto.TaskInfoDto;
import com.anst.sd.api.adapter.rest.task.write.dto.CreateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskOrderNumberDto;
import com.anst.sd.api.app.api.task.*;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.security.app.impl.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
    private final UpdateOrderNumberTaskInBound updateOrderNumberTaskInBound;
    private final ObjectMapper objectMapper;

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
            BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            throw new TaskValidationException();
        }
        Task task = taskDomainMapper.mapToDomain(request);

        String taskJson = objectMapper.writeValueAsString(task);
        System.out.println("Mapped Task object as JSON:");
        System.out.println(taskJson);

        Task result = createTaskInBound.create(jwtService.getJwtAuth().getUserId(), projectId, task);
        return ResponseEntity.ok(taskDtoMapper.mapToDto(result));
    }

    @Operation(
        summary = "Update number order task",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Task number order updated successfully",
                useReturnTypeSchema = true)
        })
    @PutMapping("/{id}/orderNumber")
    public ResponseEntity<IdResponseDto> updateOrderNumberTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskOrderNumberDto request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new TaskValidationException(id);
        }
        BigDecimal orderNumber = request.getOrderNumber();
        Task result = updateOrderNumberTaskInBound.updateOrderNumber(jwtService.getJwtAuth().getUserId(), id, orderNumber);
        return ResponseEntity.ok(new IdResponseDto(result.getId()));
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
