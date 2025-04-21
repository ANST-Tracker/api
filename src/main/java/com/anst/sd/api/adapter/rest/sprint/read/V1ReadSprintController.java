package com.anst.sd.api.adapter.rest.sprint.read;

import com.anst.sd.api.adapter.rest.sprint.dto.SprintDtoMapper;
import com.anst.sd.api.adapter.rest.sprint.dto.SprintInfoDto;
import com.anst.sd.api.app.api.sprint.GetSprintsByProjectInbound;
import com.anst.sd.api.domain.sprint.Sprint;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "SprintController")
@Slf4j
@RestController
@RequestMapping("/project/{projectId}/sprint")
@RequiredArgsConstructor
public class V1ReadSprintController {
    private final JwtService jwtService;
    private final SprintDtoMapper sprintDtoMapper;
    private final GetSprintsByProjectInbound getSprintsByProjectInbound;

    @Operation(
            summary = "Get all sprints information by user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/all")
    public ResponseEntity<List<SprintInfoDto>> getSprints(@PathVariable UUID projectId) {
        List<Sprint> sprints = getSprintsByProjectInbound.get(jwtService.getJwtAuth().getUserId(), projectId);
        return ResponseEntity.ok(sprintDtoMapper.mapToDto(sprints));
    }
}
