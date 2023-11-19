package com.anst.sd.api.adapter.rest.task;

import com.anst.sd.api.adapter.rest.task.dto.*;
import com.anst.sd.api.app.api.task.*;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/task")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final GetTasksByUserInBound getTasksByUserInBound;
    private final GetTaskByUserInBound getTaskByUserInBound;
    private final UpdateTaskByUserInBound updateTaskByUserInBound;
    private final DeleteTaskByUserInBound deleteTaskByUserInBound;
    private final FilterTasksByUserInBound filterTasksByUserInBound;
    private final JwtService jwtService;
    private final CreateTaskByUserInBound createTaskByUserInBound;
    private final TaskInfoDtoMapper taskInfoDtoMapper;
    private final TaskDomainMapper taskDomainMapper;
    private final TaskDtoMapper taskDtoMapper;
    private final FilterRequestDomainMapper filterRequestDomainMapper;

    @Operation(
            summary = "Create a new task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task created successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfoDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data")
            })
    @PostMapping("/create")
    public ResponseEntity<TaskInfoDto> createTask(@Valid @RequestBody CreateTaskRequest request) {
        Task task = taskDomainMapper.mapToDomain(request);
        Task result = createTaskByUserInBound.createTask(jwtService.getJwtAuth().getUserId(), task);
        TaskInfoDto response = taskInfoDtoMapper.mapToDto(result);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get task by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task received successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfoDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<TaskInfoDto> getTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        Optional<Task> task = getTaskByUserInBound.getTask(jwtService.getJwtAuth().getUserId(), id);
        TaskInfoDto response = taskDtoMapper.mapToDto(task.get());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task updated successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfoDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data")
            })
    @PutMapping("/update")
    public ResponseEntity<TaskInfoDto> updateTask(@Valid @RequestBody UpdateTaskRequestDto request) {
        Task task = taskDomainMapper.mapToDomain(request);
        Task result = updateTaskByUserInBound.updateTask(jwtService.getJwtAuth().getUserId(), task);
        TaskInfoDto response = taskDtoMapper.mapToDto(result);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete task by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task deleted successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfoDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskInfoDto> deleteTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        Optional<Task> task = deleteTaskByUserInBound.deleteTask(jwtService.getJwtAuth().getUserId(), id);
        TaskInfoDto response = taskDtoMapper.mapToDto(task.get());
        return ResponseEntity.ok(response);
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
        List<Task> tasks = getTasksByUserInBound.getTasks(jwtService.getJwtAuth().getUserId(), page);
        List<TaskInfoDto> response = taskDtoMapper.mapToDto(tasks);
        return ResponseEntity.ok(response);
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
        FilterRequest domain = filterRequestDomainMapper.mapToDomain(filterRequestDto);
        List<Task> process = filterTasksByUserInBound.filterTasks(jwtService.getJwtAuth().getUserId(), domain);
        List<TaskInfoDto> result = taskInfoDtoMapper.mapToDto(process);
        return ResponseEntity.ok(result);
    }
}