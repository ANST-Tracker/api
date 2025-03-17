//package com.anst.sd.api.adapter.rest.project.read;
//
//import com.anst.sd.api.adapter.rest.project.dto.ProjectInfoDto;
//import com.anst.sd.api.adapter.rest.project.dto.ProjectInfoDtoMapper;
//import com.anst.sd.api.app.api.project.GetProjectInbound;
//import com.anst.sd.api.domain.project.Project;
//import com.anst.sd.api.security.app.impl.JwtService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@Slf4j
//@RestController
//@RequestMapping("/project")
//@RequiredArgsConstructor
//public class V1ReadProjectController {
//    private final JwtService jwtService;
//    private final GetProjectInbound getProjectsInbound;
//    private final ProjectInfoDtoMapper projectInfoDtoMapper;
//
//    @Operation(
//            summary = "Get project",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            useReturnTypeSchema = true)
//            })
//    @GetMapping("/list")
//    public ResponseEntity<List<ProjectInfoDto>> getProjects() {
//        List<Project> projects = getProjectsInbound.get(jwtService.getJwtAuth().getUserId());
//        return ResponseEntity.ok(projectInfoDtoMapper.mapToDto(projects));
//    }
//}
