package com.anst.sd.api.adapter.rest.project.read;

import com.anst.sd.api.adapter.rest.project.dto.ProjectInfoDto;
import com.anst.sd.api.adapter.rest.project.dto.ProjectInfoDtoMapper;
import com.anst.sd.api.app.api.project.GetProjectInbound;
import com.anst.sd.api.domain.project.Project;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class V1ReadProjectController {
    private final GetProjectInbound getProjectInbound;
    private final ProjectInfoDtoMapper projectInfoDtoMapper;

    @Operation(
            summary = "Get project information",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectInfoDto> getProjects(@PathVariable UUID projectId) {
        Project project = getProjectInbound.get(projectId);
        return ResponseEntity.ok(projectInfoDtoMapper.mapToDto(project));
    }
}
