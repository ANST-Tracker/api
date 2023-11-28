package com.anst.sd.api.adapter.rest.task;

import com.anst.sd.api.adapter.rest.task.dto.*;
import com.anst.sd.api.app.api.task.*;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/task")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final GetTasksInBound getTasksInBound;
    private final GetTaskInBound getTaskInBound;
    private final UpdateTaskInBound updateTaskInBound;
    private final DeleteTaskInBound deleteTaskInBound;
    private final FilterTasksInBound filterTasksInBound;
    private final JwtService jwtService;
    private final CreateTaskInBound createTaskInBound;
    private final TaskDtoMapper taskDtoMapper;
    private final TaskDomainMapper taskDomainMapper;
    private final FilterRequestDomainMapper filterRequestDomainMapper;

    @Operation(
            summary = "Create a new task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task created successfully",
                            useReturnTypeSchema = true)
            })
    @PostMapping("/create")
    public ResponseEntity<TaskInfoDto> createTask(@Valid @RequestBody CreateTaskRequestDto request) {
        Task task = taskDomainMapper.mapToDomain(request);
        Task result = createTaskInBound.createTask(jwtService.getJwtAuth().getUserId(), task);
        return ResponseEntity.ok(taskDtoMapper.mapToDto(result));
    }

    @Operation(
            summary = "Get task by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task received successfully",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/{id}")
    public ResponseEntity<TaskInfoDto> getTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        Task task = getTaskInBound.getTask(jwtService.getJwtAuth().getUserId(), id);
        return ResponseEntity.ok(taskDtoMapper.mapToDto(task));
    }

    @Operation(
            summary = "Update task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task updated successfully",
                            useReturnTypeSchema = true)
            })
    @PutMapping("/update")
    public ResponseEntity<TaskInfoDto> updateTask(@Valid @RequestBody UpdateTaskRequestDto request) {
        Task task = taskDomainMapper.mapToDomain(request);
        Task result = updateTaskInBound.updateTask(jwtService.getJwtAuth().getUserId(), task);
        return ResponseEntity.ok(taskDtoMapper.mapToDto(result));
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
    public ResponseEntity<TaskInfoDto> deleteTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        Task task = deleteTaskInBound.deleteTask(jwtService.getJwtAuth().getUserId(), id);
        return ResponseEntity.ok(taskDtoMapper.mapToDto(task));
    }

    @Operation(
            summary = "Get list of tasks",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tasks retrieved successfully",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/list")
    public ResponseEntity<List<TaskInfoDto>> getTasks(Integer page) {
        List<Task> tasks = getTasksInBound.getTasks(jwtService.getJwtAuth().getUserId(), page);
        return ResponseEntity.ok(taskDtoMapper.mapToDto(tasks));
    }

    @Operation(
            summary = "Filter tasks by criteria",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tasks filtered successfully",
                            useReturnTypeSchema = true)
            })
    @GetMapping(value = "/filter")
    public ResponseEntity<List<TaskInfoDto>> searchTasks(@RequestBody FilterRequestDto filterRequestDto) {
        FilterRequest filterRequest = filterRequestDomainMapper.mapToDomain(filterRequestDto);
        List<Task> result = filterTasksInBound.filterTasks(jwtService.getJwtAuth().getUserId(), filterRequest);
        return ResponseEntity.ok(taskDtoMapper.mapToDto(result));
    }
}