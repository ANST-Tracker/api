package com.anst.sd.api.adapter.rest.users_projects.write;

import com.anst.sd.api.adapter.rest.dto.IdResponseDto;
import com.anst.sd.api.adapter.rest.users_projects.write.dto.AddUserInProjectDto;
import com.anst.sd.api.adapter.rest.users_projects.write.dto.UsersProjectsDomainMapper;
import com.anst.sd.api.app.api.users_projects.AddUserInProjectInBound;
import com.anst.sd.api.app.api.users_projects.RemoveUserFromProjectInBound;
import com.anst.sd.api.app.api.users_projects.UsersProjectsValidationException;
import com.anst.sd.api.domain.UsersProjects;
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

@Tag(name = "UserProjectsController")
@Slf4j
@RequestMapping("/users-projects")
@RestController
@RequiredArgsConstructor
public class V1WriteUsersProjectsController {
    private final AddUserInProjectInBound addUserInProjectInBound;
    private final RemoveUserFromProjectInBound removeUserFromProjectInBound;
    private final JwtService jwtService;
    private final UsersProjectsDomainMapper usersProjectsDomainMapper;

    @Operation(
            summary = "Add user in project",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User added in project successfully",
                            useReturnTypeSchema = true)
            })
    @PostMapping
    public ResponseEntity<IdResponseDto> addUserInProject(
            @Valid @RequestBody AddUserInProjectDto request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new UsersProjectsValidationException();
        }
        UsersProjects usersProjects = usersProjectsDomainMapper.mapToDomain(request);
        UsersProjects result = addUserInProjectInBound.add(usersProjects, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new IdResponseDto(result.getId()));
    }

    @Operation(
            summary = "Remove user from project",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "User removed from project successfully"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User-Project relation not found")
            })
    @DeleteMapping("/projects/{projectId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromProject(
            @PathVariable UUID projectId,
            @PathVariable UUID userId
    ) {
        removeUserFromProjectInBound.remove(projectId, userId, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.noContent().build();
    }
}
