package com.anst.sd.api.adapter.rest.sprint.write;

import com.anst.sd.api.adapter.rest.dto.IdResponseDto;
import com.anst.sd.api.adapter.rest.sprint.dto.SprintDomainMapper;
import com.anst.sd.api.adapter.rest.sprint.read.dto.CreateSprintDto;
import com.anst.sd.api.adapter.rest.sprint.read.dto.UpdateSprintDto;
import com.anst.sd.api.app.api.sprint.AttachTaskToSprintInBound;
import com.anst.sd.api.app.api.sprint.CreateSprintInBound;
import com.anst.sd.api.app.api.sprint.SprintValidationException;
import com.anst.sd.api.app.api.sprint.UpdateSprintInBound;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sprint")
public class V1WriteSprintController {
    private final CreateSprintInBound createSprintInBound;
    private final UpdateSprintInBound updateSprintInBound;
    private final AttachTaskToSprintInBound attachTaskToSprintInBound;
    private final SprintDomainMapper sprintDomainMapper;
    private final JwtService jwtService;

    @Operation(
            summary = "Create sprint",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            }
    )
    @PostMapping
    public ResponseEntity<IdResponseDto> create(@Valid @RequestBody CreateSprintDto dto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new SprintValidationException();
        }
        Sprint sprint = sprintDomainMapper.mapToDomain(dto);
        Sprint result = createSprintInBound.create(jwtService.getJwtAuth().getUserId(), sprint);
        return ResponseEntity.ok(new IdResponseDto(result.getId()));
    }

    @Operation(
            summary = "Update sprint",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<IdResponseDto> update(@PathVariable UUID id,
                                                @Valid @RequestBody UpdateSprintDto dto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new SprintValidationException();
        }
        Sprint sprint = sprintDomainMapper.mapToDomain(dto);
        Sprint result = updateSprintInBound.update(jwtService.getJwtAuth().getUserId(), id, sprint);
        return ResponseEntity.ok(new IdResponseDto(result.getId()));
    }

    @Operation(
            summary = "Attach task to sprint",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "attached"
                    )
            }
    )
    @PutMapping("/{id}/task/{simpleId}")
    public ResponseEntity<Void> attach(@PathVariable UUID id,
                                       @PathVariable String simpleId) {
        attachTaskToSprintInBound.attach(jwtService.getJwtAuth().getUserId(), id, simpleId);
        return ResponseEntity.ok().build();
    }
}
