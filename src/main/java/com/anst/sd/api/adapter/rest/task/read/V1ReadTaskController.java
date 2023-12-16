package com.anst.sd.api.adapter.rest.task.read;

import com.anst.sd.api.adapter.rest.task.dto.TaskDtoMapper;
import com.anst.sd.api.adapter.rest.task.dto.TaskInfoDto;
import com.anst.sd.api.adapter.rest.task.read.dto.FilterRequestDomainMapper;
import com.anst.sd.api.adapter.rest.task.read.dto.TaskFilterRequestDto;
import com.anst.sd.api.app.api.task.FilterTasksInBound;
import com.anst.sd.api.app.api.task.GetTaskInBound;
import com.anst.sd.api.app.api.task.GetTasksInBound;
import com.anst.sd.api.app.api.task.TaskFilter;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/task")
@RestController
@RequiredArgsConstructor
public class V1ReadTaskController {
    private final GetTasksInBound getTasksInBound;
    private final GetTaskInBound getTaskInBound;
    private final FilterTasksInBound filterTasksInBound;
    private final JwtService jwtService;
    private final TaskDtoMapper taskDtoMapper;
    private final FilterRequestDomainMapper filterRequestDomainMapper;

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
        Task task = getTaskInBound.get(jwtService.getJwtAuth().getUserId(), id);
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
    public ResponseEntity<List<TaskInfoDto>> getTasks(@RequestParam Integer page) {
        List<Task> tasks = getTasksInBound.get(jwtService.getJwtAuth().getUserId(), page);
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
    @PostMapping("/find-by-filter")
    public ResponseEntity<List<TaskInfoDto>> searchTasks(@RequestBody TaskFilterRequestDto taskFilterRequestDto) {
        TaskFilter filterRequest = filterRequestDomainMapper.mapToDomain(taskFilterRequestDto);
        List<Task> result = filterTasksInBound.filter(jwtService.getJwtAuth().getUserId(), filterRequest);
        return ResponseEntity.ok(taskDtoMapper.mapToDto(result));
    }
}
