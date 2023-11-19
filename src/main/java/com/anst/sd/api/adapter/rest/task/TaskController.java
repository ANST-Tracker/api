package com.anst.sd.api.adapter.rest.task;

import com.anst.sd.api.adapter.rest.task.dto.TaskMapper;
import com.anst.sd.api.adapter.rest.task.dto.TaskInfo;
import com.anst.sd.api.app.api.task.*;
import com.anst.sd.api.adapter.rest.task.dto.CreateTaskRequest;
import com.anst.sd.api.adapter.rest.task.dto.FilterRequest;
import com.anst.sd.api.adapter.rest.task.dto.UpdateTaskRequest;
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
    @Operation(
            summary = "Create a new task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task created successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfo.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data")
            })
    @PostMapping("/create")
    public ResponseEntity<TaskInfo> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok
                (createTaskByUserInBound.createTask(jwtService.getJwtAuth().getUserId(), TaskMapper.toModel(request)));
    }

    @Operation(
            summary = "Get task by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task received successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfo.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<TaskInfo> getTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        return ResponseEntity.ok(getTaskByUserInBound.getTask(jwtService.getJwtAuth().getUserId(), id));
    }

    @Operation(
            summary = "Update task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task updated successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfo.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data")
            })
    @PutMapping("/update")
    public ResponseEntity<TaskInfo> updateTask(@Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(updateTaskByUserInBound.updateTask(jwtService.getJwtAuth().getUserId(),
                TaskMapper.toModel(request)));
    }

    @Operation(
            summary = "Delete task by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task deleted successfully",
                            content = @Content(schema = @Schema(implementation = TaskInfo.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskInfo> deleteTask(@Parameter(description = "Task ID") @PathVariable Long id) {
        return ResponseEntity.ok(deleteTaskByUserInBound.deleteTask(jwtService.getJwtAuth().getUserId(), id));
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
    public ResponseEntity<List<TaskInfo>> getTasks(Integer page) {
        return ResponseEntity.ok(getTasksByUserInBound.getTasks(jwtService.getJwtAuth().getUserId(), page));
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
    public ResponseEntity<List<TaskInfo>> searchTasks(@RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(filterTasksByUserInBound.filterTasks(jwtService.getJwtAuth().getUserId(), filterRequest));
    }
}