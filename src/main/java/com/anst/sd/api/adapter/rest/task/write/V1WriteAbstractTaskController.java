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

import java.util.UUID;

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
            summary = "Create a new abstract task",
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
            summary = "Update an existing abstract task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Abstract task updated successfully",
                            useReturnTypeSchema = true
                    )
            }
    )
    @PutMapping("/{taskId}")
    public ResponseEntity<IdResponseDto> update(@PathVariable UUID taskId,
                                                @Valid @RequestBody UpdateAbstractTaskDto request
    ) {
        AbstractTask task = abstractTaskDomainMapper.mapToDomain(request);
        AbstractTask result = updateAbstractTaskInBound.update(jwtService.getJwtAuth().getUserId(), taskId, task);
        return ResponseEntity.ok(abstractTaskDtoMapper.mapToDto(result));
    }

    @Operation(
            summary = "Update the status of an abstract task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Abstract task status updated successfully",
                            useReturnTypeSchema = true
                    )
            }
    )
    @PutMapping("/{taskId}/status")
    public ResponseEntity<IdResponseDto> updateStatus(
            @PathVariable UUID taskId, @RequestBody @Valid UpdateAbstractTaskStatusDto request
    ) {
        AbstractTask updated = updateAbstractTaskStatusInBound.updateStatus(
                jwtService.getJwtAuth().getUserId(),
                taskId,
                request.getStatus()
        );
        return ResponseEntity.ok(abstractTaskDtoMapper.mapToDto(updated));
    }
}