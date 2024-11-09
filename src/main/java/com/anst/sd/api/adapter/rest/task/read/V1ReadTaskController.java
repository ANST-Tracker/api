package com.anst.sd.api.adapter.rest.task.read;

import com.anst.sd.api.adapter.rest.task.read.dto.*;
import com.anst.sd.api.adapter.rest.task.dto.TaskDtoMapper;
import com.anst.sd.api.adapter.rest.task.dto.TaskInfoDto;
import com.anst.sd.api.app.api.task.*;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private final GetTasksByMonthAndYearInBound getTasksByMonthAndYearInBound;
    private final TasksByDateDtoMapper tasksByDateDtoMapper;

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
    public ResponseEntity<List<TaskInfoDto>> getTasks(
            @RequestParam(required = false) Integer page,
            @RequestParam Long projectId) {
        List<Task> tasks = getTasksInBound.get(jwtService.getJwtAuth().getUserId(), projectId, page);
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

    @Operation(
            summary = "Get list of tasks by month and year",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tasks retrieved successfully",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/by-month-year")
    public ResponseEntity<List<TasksByDateDto>> getTasksByMonthAndYear(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        Map<LocalDate, List<Task>> tasksByDate = getTasksByMonthAndYearInBound.getTasksByMonthAndYear(
                jwtService.getJwtAuth().getUserId(), month, year);
        List<TasksByDateDto> taskByDateDto = tasksByDateDtoMapper.mapToDto(tasksByDate);
        return ResponseEntity.ok(taskByDateDto);
    }
}
