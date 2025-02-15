package com.anst.sd.api.adapter.rest.task.write;

import com.anst.sd.api.adapter.rest.task.dto.AbstractTaskDomainMapper;
import com.anst.sd.api.adapter.rest.task.dto.AbstractTaskDtoMapper;
import com.anst.sd.api.adapter.rest.task.dto.IdResponseDto;
import com.anst.sd.api.adapter.rest.task.write.dto.CreateAbstractTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateAbstractTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateAbstractTaskStatusDto;
import com.anst.sd.api.app.api.task.CreateAbstractTaskInBound;
import com.anst.sd.api.app.api.task.UpdateAbstractTaskInBound;
import com.anst.sd.api.app.api.task.UpdateAbstractTaskStatusInBound;
import com.anst.sd.api.domain.task.AbstractTask;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/task")
@RestController
@RequiredArgsConstructor
public class V1WriteAbstractTaskController {
    private final CreateAbstractTaskInBound createAbstractTaskInBound;
    private final UpdateAbstractTaskInBound updateAbstractTaskInBound;
    private final UpdateAbstractTaskStatusInBound updateAbstractTaskStatusInBound;
    private final JwtService jwtService;
    private final AbstractTaskDomainMapper abstractTaskDomainMapper;
    private final AbstractTaskDtoMapper abstractTaskDtoMapper;

    @Operation(
            summary = "Create a new task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Abstract task created successfully",
                            useReturnTypeSchema = true
                    )
            }
    )
    @PostMapping
    public ResponseEntity<IdResponseDto> create(@Valid @RequestBody CreateAbstractTaskDto request) {
        AbstractTask task = abstractTaskDomainMapper.mapToDomain(request);
        AbstractTask result = createAbstractTaskInBound.create(jwtService.getJwtAuth().getUserId(), task);
        return ResponseEntity.ok(abstractTaskDtoMapper.mapToDto(result));
    }

    @Operation(
            summary = "Update an existing task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task updated successfully",
                            useReturnTypeSchema = true
                    )
            }
    )
    @PutMapping("/{simpleId}")
    public ResponseEntity<IdResponseDto> update(@PathVariable String simpleId,
                                                @Valid @RequestBody UpdateAbstractTaskDto request
    ) {
        AbstractTask task = abstractTaskDomainMapper.mapToDomain(request);
        AbstractTask result = updateAbstractTaskInBound.update(jwtService.getJwtAuth().getUserId(), simpleId, task);
        return ResponseEntity.ok(abstractTaskDtoMapper.mapToDto(result));
    }

    @Operation(
            summary = "Update the status of task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task status updated successfully",
                            useReturnTypeSchema = true
                    )
            }
    )
    @PutMapping("/{simpleId}/status")
    public ResponseEntity<IdResponseDto> updateStatus(
            @PathVariable String simpleId, @RequestBody @Valid UpdateAbstractTaskStatusDto request
    ) {
        AbstractTask updated = updateAbstractTaskStatusInBound.updateStatus(
                jwtService.getJwtAuth().getUserId(),
                simpleId,
                request.getStatus().toString()
        );
        return ResponseEntity.ok(abstractTaskDtoMapper.mapToDto(updated));
    }
}