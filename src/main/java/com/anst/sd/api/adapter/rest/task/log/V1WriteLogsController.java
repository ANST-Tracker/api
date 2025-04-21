package com.anst.sd.api.adapter.rest.task.log;

import com.anst.sd.api.adapter.rest.dto.IdResponseDto;
import com.anst.sd.api.adapter.rest.task.log.dto.CreateUpdateLogDto;
import com.anst.sd.api.adapter.rest.task.log.dto.LogDomainMapper;
import com.anst.sd.api.app.api.task.log.CreateLogInBound;
import com.anst.sd.api.app.api.task.log.DeleteLogInBound;
import com.anst.sd.api.app.api.task.log.LogValidationException;
import com.anst.sd.api.app.api.task.log.UpdateLogInBound;
import com.anst.sd.api.domain.task.Log;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "LogController")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("project/{projectId}/task/{simpleId}/logs")
public class V1WriteLogsController {
    private final JwtService jwtService;
    private final CreateLogInBound createLogInbound;
    private final UpdateLogInBound updateLogInbound;
    private final DeleteLogInBound deleteLogInbound;
    private final LogDomainMapper logDomainMapper;

    @Operation(
            summary = "Create a new log",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @PostMapping
    public ResponseEntity<IdResponseDto> create(@PathVariable UUID projectId,
                                                @PathVariable String simpleId,
                                                @Valid @RequestBody CreateUpdateLogDto request,
                                                BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new LogValidationException();
        }

        Log log = createLogInbound.create(
                logDomainMapper.mapToDomain(request),
                projectId,
                simpleId,
                jwtService.getJwtAuth().getUserId());

        return ResponseEntity.ok(new IdResponseDto(log.getId()));
    }

    @Operation(summary = "Update an existing log")
    @PutMapping("/{id}")
    public ResponseEntity<IdResponseDto> update(@PathVariable UUID projectId,
                                                @PathVariable String simpleId,
                                                @PathVariable UUID id,
                                                @Valid @RequestBody CreateUpdateLogDto request,
                                                BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new LogValidationException(simpleId);
        }
        Log updateLog = logDomainMapper.mapToDomain(request);
        updateLog.setId(id);
        Log log = updateLogInbound.update(
                updateLog,
                projectId,
                simpleId,
                jwtService.getJwtAuth().getUserId());

        return ResponseEntity.ok(new IdResponseDto(log.getId()));
    }

    @Operation(
            summary = "Delete a log",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<IdResponseDto> delete(@PathVariable UUID projectId,
                                                @PathVariable String simpleId,
                                                @PathVariable UUID id
    ) {
        Log log = deleteLogInbound.delete(id, projectId, simpleId, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new IdResponseDto(log.getId()));
    }
}


