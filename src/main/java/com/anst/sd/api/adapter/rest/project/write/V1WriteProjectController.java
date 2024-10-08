package com.anst.sd.api.adapter.rest.project.write;

import com.anst.sd.api.adapter.rest.dto.IdResponseDto;
import com.anst.sd.api.adapter.rest.project.write.dto.CreateProjectDto;
import com.anst.sd.api.adapter.rest.project.write.dto.ProjectDomainMapper;
import com.anst.sd.api.adapter.rest.project.write.dto.UpdateProjectDto;
import com.anst.sd.api.app.api.project.CreateProjectInbound;
import com.anst.sd.api.app.api.project.DeleteProjectInbound;
import com.anst.sd.api.app.api.project.ProjectValidationException;
import com.anst.sd.api.app.api.project.UpdateProjectInbound;
import com.anst.sd.api.domain.project.Project;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class V1WriteProjectController {
    private final JwtService jwtService;
    private final DeleteProjectInbound deleteProjectInbound;
    private final CreateProjectInbound createProjectInbound;
    private final UpdateProjectInbound updateProjectInbound;
    private final ProjectDomainMapper projectDomainMapper;

    @Operation(
            summary = "Delete project by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<IdResponseDto> deleteProject(@PathVariable Long id) {
        Project project = deleteProjectInbound.delete(id, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new IdResponseDto(project.getId()));
    }

    @Operation(
            summary = "Create project",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @PostMapping
    public ResponseEntity<IdResponseDto> createProject(
            @Valid @RequestBody CreateProjectDto request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ProjectValidationException();
        }
        Project project = projectDomainMapper.mapToDomain(request);
        Project result = createProjectInbound.create(project, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new IdResponseDto(result.getId()));
    }

    @Operation(
            summary = "Update project",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @PutMapping("/{id}")
    public ResponseEntity<IdResponseDto> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectDto request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ProjectValidationException(id);
        }
        Project project = projectDomainMapper.mapToDomain(request);
        Project result = updateProjectInbound.update(id, project, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new IdResponseDto(result.getId()));
    }
}
