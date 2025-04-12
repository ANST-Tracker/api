package com.anst.sd.api.adapter.rest.task.read;

import com.anst.sd.api.adapter.rest.task.dto.*;
import com.anst.sd.api.app.api.filter.FilterValidationException;
import com.anst.sd.api.app.api.task.FindTasksByFilterInbound;
import com.anst.sd.api.app.api.task.GetTaskInbound;
import com.anst.sd.api.domain.filter.Filter;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TaskController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class ReadTaskController {
    private final TaskRegistryDtoMapper taskRegistryDtoMapper;
    private final JwtService jwtService;
    private final FindTasksByFilterInbound findTasksByFilterInbound;
    private final TaskFilterDomainMapper taskFilterDomainMapper;
    private final GetTaskInbound getTaskInbound;
    private final TaskInfoDtoMapper taskInfoDtoMapper;

    @Operation(
        summary = "Find tasks by filter",
        responses = {
            @ApiResponse(
                responseCode = "200",
                useReturnTypeSchema = true)
        })
    @PostMapping("/find-by-filter")
    public ResponseEntity<List<TaskRegistryDto>> findByFilter(
        @Valid @RequestBody TaskFilterDto taskFilterDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FilterValidationException();
        }
        Filter filter = taskFilterDomainMapper.mapToDomain(taskFilterDto);
        filter.setUserId(jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(findTasksByFilterInbound.find(filter).stream()
            .map(taskRegistryDtoMapper::mapToDto)
            .toList()
        );
    }

    @Operation(
            summary = "Get task by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @ApiResponse(
            responseCode = "200",
            description = "Task got successfully",
            content = @Content(schema = @Schema(oneOf = {
                    DefectTaskInfoDto.class,
                    SubtaskInfoDto.class,
                    StoryTaskInfoDto.class,
                    EpicTaskInfoDto.class
            })))
    @GetMapping("/{simpleId}")
    public ResponseEntity<Object> getById(@PathVariable String simpleId) {
        AbstractTask task = getTaskInbound.get(simpleId, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(taskInfoDtoMapper.mapToDtoAuto(task));
    }
}
